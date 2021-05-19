package unity.annotations;

import arc.struct.*;
import arc.struct.ObjectMap.*;
import arc.util.*;
import arc.util.pooling.Pool.*;
import com.squareup.javapoet.*;
import com.sun.source.tree.*;
import mindustry.gen.*;
import unity.annotations.Annotations.*;
import unity.annotations.EntityProcessor.*;
import unity.annotations.TypeIOResolver.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import java.util.*;
import java.util.regex.*;

import static javax.lang.model.type.TypeKind.*;

/** @author GlennFolker */
@SuppressWarnings("unchecked")
@SupportedAnnotationTypes({
    "unity.annotations.Annotations.Merge",
    "unity.annotations.Annotations.MergeComponent",
    "unity.annotations.Annotations.MergeInterface"
})
public class MergeProcessor extends BaseProcessor{
    Seq<TypeElement> comps = new Seq<>();
    Seq<TypeElement> inters = new Seq<>();
    Seq<Element> defs = new Seq<>();
    ObjectMap<TypeElement, ObjectMap<String, Seq<ExecutableElement>>> inserters = new ObjectMap<>();
    StringMap varInitializers = new StringMap();
    StringMap methodBlocks = new StringMap();
    ObjectMap<String, Seq<String>> imports = new ObjectMap<>();
    ObjectMap<TypeElement, Seq<TypeElement>> componentDependencies = new ObjectMap<>();
    Seq<MergeDefinition> definitions = new Seq<>();
    ClassSerializer serializer;

    {
        rounds = 3;
    }

    @Override
    public void process(RoundEnvironment roundEnv) throws Exception{
        comps = comps.addAll((Set<TypeElement>)roundEnv.getElementsAnnotatedWith(MergeComponent.class)).flatMap(t -> Seq.with(t).and(types(t)));
        inters = inters.addAll((Set<TypeElement>)roundEnv.getElementsAnnotatedWith(MergeInterface.class)).flatMap(t -> Seq.with(t).and(types(t)));
        defs.addAll(roundEnv.getElementsAnnotatedWith(Merge.class));

        for(ExecutableElement e : (Set<ExecutableElement>)roundEnv.getElementsAnnotatedWith(Insert.class)){
            if(!e.getParameters().isEmpty()) throw new IllegalStateException("All @Insert methods must not have parameters");

            TypeElement type = comps.find(c -> simpleName(c).equals(simpleName(e.getEnclosingElement())));
            if(type == null) continue;

            Insert ann = annotation(e, Insert.class);
            inserters
                .get(type, ObjectMap::new)
                .get(ann.value(), Seq::new)
                .add(e);
        }

        if(round == 1){
            serializer = TypeIOResolver.resolve(this);

            for(TypeElement comp : comps.select(t -> t.getEnclosingElement() instanceof PackageElement)){
                TypeSpec.Builder builder = toInterface(comp, getDependencies(comp))
                    .addAnnotation(
                        AnnotationSpec.builder(cName(SuppressWarnings.class))
                            .addMember("value", "$S", "all")
                        .build()
                    );

                Seq<TypeElement> types = types(comp);
                if(types.size > 1){
                    throw new IllegalStateException("@MergeComponent can't have more than 1 nested class! The nested class must be the building type");
                }

                TypeElement buildType = types.isEmpty() ? null : types.first();
                if(buildType != null){
                    if(!typeUtils.isAssignable(
                        buildType.asType(),
                        elementUtils.getTypeElement(Building.class.getCanonicalName()).asType()
                    )){
                        throw new IllegalStateException("@MergeComponent class' nested class must be the building type");
                    }

                    TypeSpec.Builder subBuilder = toInterface(buildType, getDependencies(buildType));
                    builder.addType(subBuilder.build());

                    builder.addAnnotation(
                        AnnotationSpec.builder(cName(MergeInterface.class))
                            .addMember("value", "$L.$L.class", interfaceName(comp), interfaceName(buildType))
                        .build()
                    );
                }else{
                    builder.addAnnotation(cName(MergeInterface.class));
                }

                write(builder.build());
            }
        }else if(round == 2){
            ObjectMap<String, Element> usedNames = new ObjectMap<>();
            for(Element def : defs){
                Merge ann = annotation(def, Merge.class);

                Seq<TypeElement> defComps = elements(ann::value)
                    .<TypeElement>as()
                    .map(t -> inters.find(i -> simpleName(i).equals(simpleName(t))))
                    .select(Objects::nonNull)
                    .select(t -> t.getEnclosingElement() instanceof PackageElement)
                    .map(this::toComp);

                if(defComps.isEmpty()) continue;

                MergeDefinition definition = toClass(def, usedNames, defComps);
                if(definition == null) continue;

                defComps = defComps
                    .map(t -> inters.find(i -> simpleName(i).equals(interfaceName(t))))
                    .select(Objects::nonNull)
                    .map(t -> elements(annotation(t, MergeInterface.class)::value).<TypeElement>as())
                    .select(t -> t != null && !t.isEmpty())
                    .map(s -> toComp(s.first()));

                MergeDefinition buildDefinition = toClass(def, usedNames, defComps);
                buildDefinition.parent = definition;

                definitions.add(definition, buildDefinition);
            }
        }else if(round == 3){
            for(MergeDefinition def : definitions){
                if(processDefinition(def)){
                    write(def.builder.build(), def.components.flatMap(comp -> imports.get(interfaceName(comp))));
                }
            }
        }
    }

    TypeSpec.Builder toInterface(TypeElement comp, Seq<TypeElement> depends){
        boolean isBuild = comp.getEnclosingElement() instanceof TypeElement;
        TypeSpec.Builder inter = TypeSpec.interfaceBuilder(interfaceName(comp)).addModifiers(Modifier.PUBLIC);

        if(isBuild){
            inter.addModifiers(Modifier.STATIC);
        }

        for(TypeElement extraInterface : Seq.with(comp.getInterfaces()).map(BaseProcessor::toEl).<TypeElement>as().select(i -> !isCompInterface(i))){
            inter.addSuperinterface(cName(extraInterface));
        }

        for(TypeElement type : depends){
            inter.addSuperinterface(cName(type));
        }

        for(ExecutableElement m : methods(comp)){
            if(is(m, Modifier.ABSTRACT, Modifier.NATIVE)) continue;

            methodBlocks.put(descString(m), treeUtils.getTree(m).getBody().toString()
                .replaceAll("this\\.<(.*)>self\\(\\)", "this")
                .replaceAll("self\\(\\)(?!\\s+instanceof)", "this")
                .replaceAll(" yield ", "")
                .replaceAll("\\/\\*missing\\*\\/", "var")
            );
        }

        for(VariableElement var : vars(comp)){
            VariableTree tree = (VariableTree)treeUtils.getTree(var);
            if(tree.getInitializer() != null){
                varInitializers.put(descString(var), tree.getInitializer().toString());
            }
        }

        if(!isBuild) imports.put(interfaceName(comp), getImports(comp));
        ObjectSet<String> preserved = new ObjectSet<>();

        for(ExecutableElement m : methods(comp).select(me -> !isConstructor(me) && !is(me, Modifier.PRIVATE, Modifier.STATIC))){
            String name = simpleName(m);
            preserved.add(m.toString());

            if(annotation(m, Override.class) == null){
                inter.addMethod(
                    MethodSpec.methodBuilder(name)
                        .addTypeVariables(Seq.with(m.getTypeParameters()).map(TypeVariableName::get))
                        .addExceptions(Seq.with(m.getThrownTypes()).map(TypeName::get))
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameters(Seq.with(m.getParameters()).map(ParameterSpec::get))
                        .returns(TypeName.get(m.getReturnType()))
                    .build()
                );
            }
        }

        for(VariableElement var : vars(comp).select(v -> !is(v, Modifier.STATIC) && !is(v, Modifier.PRIVATE) && annotation(v, Import.class) == null)){
            String name = simpleName(var);

            if(!preserved.contains(name + "()")){
                inter.addMethod(
                    MethodSpec.methodBuilder(name)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(tName(var))
                    .build()
                );
            }

            if(
                !is(var, Modifier.FINAL) &&
                    !preserved.contains(name + "(" + var.asType().toString() + ")") &&
                    annotation(var, ReadOnly.class) == null
            ){
                inter.addMethod(
                    MethodSpec.methodBuilder(name)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(tName(var), name)
                        .returns(TypeName.VOID)
                    .build()
                );
            }
        }

        return inter;
    }

    MergeDefinition toClass(Element def, ObjectMap<String, Element> usedNames, Seq<TypeElement> defComps){
        ObjectMap<String, Seq<ExecutableElement>> methods = new ObjectMap<>();
        ObjectMap<FieldSpec, VariableElement> specVariables = new ObjectMap<>();
        ObjectSet<String> usedFields = new ObjectSet<>();

        Seq<TypeElement> naming = Seq.with(defComps);
        naming.insert(0, (TypeElement)elements(annotation(def, Merge.class)::base).first());

        String name = createName(naming);
        if(usedNames.containsKey(name)) return null;

        defComps.addAll(defComps.copy().flatMap(this::getDependencies)).distinct();

        TypeSpec.Builder builder = TypeSpec.classBuilder(name).addModifiers(Modifier.PUBLIC);

        Seq<VariableElement> allFields = new Seq<>();
        Seq<FieldSpec> allFieldSpecs = new Seq<>();

        ObjectMap<String, Seq<ExecutableElement>> ins = new ObjectMap<>();

        for(TypeElement comp : defComps){
            ObjectMap<String, Seq<ExecutableElement>> insComp = inserters.get(comp, ObjectMap::new);
            for(String s : insComp.keys()){
                ins.get(s, Seq::new).addAll(insComp.get(s));
            }

            Seq<VariableElement> fields = vars(comp).select(v -> annotation(v, Import.class) == null);
            for(VariableElement field : fields){
                if(!usedFields.add(simpleName(field))){
                    throw new IllegalStateException("Field '" + simpleName(field) + "' of component '" + simpleName(comp) + "' redefines a field in entity '" + simpleName(def) + "'");
                }

                FieldSpec.Builder fbuilder = FieldSpec.builder(tName(field), simpleName(field));

                if(is(field, Modifier.STATIC)){
                    fbuilder.addModifiers(Modifier.STATIC);
                    if(is(field, Modifier.FINAL)) fbuilder.addModifiers(Modifier.FINAL);
                }

                if(is(field, Modifier.TRANSIENT)){
                    fbuilder.addModifiers(Modifier.TRANSIENT);
                }

                if(varInitializers.containsKey(descString(field))){
                    fbuilder.initializer(varInitializers.get(descString(field)));
                }

                if(is(field, Modifier.PRIVATE)){
                    fbuilder.addModifiers(Modifier.PRIVATE);
                }else{
                    fbuilder.addModifiers(annotation(field, ReadOnly.class) != null ? Modifier.PROTECTED : Modifier.PUBLIC);
                }

                fbuilder.addAnnotations(Seq.with(field.getAnnotationMirrors()).map(AnnotationSpec::get));
                FieldSpec spec = fbuilder.build();

                builder.addField(spec);

                specVariables.put(spec, field);
                allFieldSpecs.add(spec);
                allFields.add(field);
            }

            for(ExecutableElement elem : methods(comp).select(m -> !isConstructor(m))){
                methods.get(elem.toString(), Seq::new).add(elem);
            }
        }

        builder.addMethod(
            MethodSpec.methodBuilder("toString")
                .addAnnotation(cName(Override.class))
                .returns(String.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $S + $L", name + "#", "id")
            .build()
        );

        EntityIO io = new EntityIO(simpleName(def), builder, serializer);

        for(Entry<String, Seq<ExecutableElement>> entry : methods){
            if(entry.value.contains(m -> annotation(m, Replace.class) != null)){
                if(entry.value.first().getReturnType().getKind() == VOID){
                    entry.value = entry.value.select(m -> annotation(m, Replace.class) != null);
                }else{
                    if(entry.value.count(m -> annotation(m, Replace.class) != null) > 1){
                        throw new IllegalStateException("Type " + simpleName(def) + " has multiple components replacing non-void method " + entry.key + ".");
                    }

                    ExecutableElement base = entry.value.find(m -> annotation(m, Replace.class) != null);
                    entry.value.clear();
                    entry.value.add(base);
                }
            }

            if(entry.value.count(m -> !is(m, Modifier.NATIVE, Modifier.ABSTRACT) && m.getReturnType().getKind() != VOID) > 1){
                throw new IllegalStateException("Type " + simpleName(def) + " has multiple components implementing non-void method " + entry.key + ".");
            }

            entry.value.sort(Structs.comps(Structs.comparingFloat(m -> annotation(m, MethodPriority.class) != null ? annotation(m, MethodPriority.class).value() : 0), Structs.comparing(BaseProcessor::simpleName)));

            ExecutableElement first = entry.value.first();

            if(annotation(first, InternalImpl.class) != null) continue;

            boolean isPrivate = is(first, Modifier.PRIVATE);
            MethodSpec.Builder mbuilder = MethodSpec.methodBuilder(simpleName(first)).addModifiers(isPrivate ? Modifier.PRIVATE : Modifier.PUBLIC);
            if(!isPrivate) mbuilder.addAnnotation(cName(Override.class));

            if(is(first, Modifier.STATIC)) mbuilder.addModifiers(Modifier.STATIC);
            mbuilder.addTypeVariables(Seq.with(first.getTypeParameters()).map(TypeVariableName::get));
            mbuilder.returns(TypeName.get(first.getReturnType()));
            mbuilder.addExceptions(Seq.with(first.getThrownTypes()).map(TypeName::get));

            for(VariableElement var : first.getParameters()){
                mbuilder.addParameter(tName(var), simpleName(var));
            }

            boolean writeBlock = first.getReturnType().getKind() == VOID && entry.value.size > 1;

            if((is(entry.value.first(), Modifier.ABSTRACT) || is(entry.value.first(), Modifier.NATIVE)) && entry.value.size == 1 && annotation(entry.value.first(), InternalImpl.class) == null){
                throw new IllegalStateException(simpleName(entry.value.first().getEnclosingElement()) + "#" + entry.value.first() + " is an abstract method and must be implemented in some component");
            }

            Seq<ExecutableElement> inserts = ins.get(entry.key, Seq::new);
            if(first.getReturnType().getKind() != VOID && !inserts.isEmpty()){
                throw new IllegalStateException("Method " + entry.key + " is not void, therefore no methods can @Insert to it");
            }

            Seq<ExecutableElement> noComp = inserts.select(e -> typeUtils.isSameType(
                elements(annotation(e, Insert.class)::block).first().asType(),
                elementUtils.getTypeElement("java.lang.Void").asType()
            ));

            Seq<ExecutableElement> noCompBefore = noComp.select(e -> !annotation(e, Insert.class).after());
            noCompBefore.sort(Structs.comps(Structs.comparingFloat(m -> annotation(m, MethodPriority.class) != null ? annotation(m, MethodPriority.class).value() : 0), Structs.comparing(BaseProcessor::simpleName)));

            Seq<ExecutableElement> noCompAfter = noComp.select(e -> annotation(e, Insert.class).after());
            noCompAfter.sort(Structs.comps(Structs.comparingFloat(m -> annotation(m, MethodPriority.class) != null ? annotation(m, MethodPriority.class).value() : 0), Structs.comparing(BaseProcessor::simpleName)));

            inserts = inserts.select(e -> !noComp.contains(e));

            for(ExecutableElement e : noCompBefore){
                mbuilder.addStatement("this.$L()", simpleName(e));
            }

            if(simpleName(first).equals("read") || (simpleName(first).equals("write") && first.getParameters().size() == 2)){
                io.write(mbuilder, simpleName(first).equals("write"), allFields);
            }

            boolean firstc = append(mbuilder, entry.value, inserts, writeBlock, first);

            if(!firstc && !noCompAfter.isEmpty()) mbuilder.addCode(lnew());
            for(ExecutableElement e : noCompAfter){
                mbuilder.addStatement("this.$L()", simpleName(e));
            }

            builder.addMethod(mbuilder.build());
        }

        builder.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PROTECTED).build());
        builder.addMethod(
            MethodSpec.methodBuilder("create").addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.get(packageName, name))
                .addStatement("return new $L()", name)
            .build()
        );

        return new MergeDefinition(packageName + "." + name, builder, def, defComps, allFieldSpecs);
    }

    boolean processDefinition(MergeDefinition def){
        ObjectSet<String> methodNames = def.components.flatMap(type -> methods(type).map(BaseProcessor::simpleString)).asSet();

        for(TypeElement comp : def.components){
            TypeElement inter = inters.find(i -> simpleName(i).equals(interfaceName(comp)));
            if(inter == null){
                throw new IllegalStateException("Failed to generate interface for " + simpleName(comp));
            }

            def.builder.addSuperinterface(cName(inter));

            for(ExecutableElement method : methods(inter)){
                String var = simpleName(method);
                FieldSpec field = Seq.with(def.fieldSpecs).find(f -> f.name.equals(var));

                if(field == null || methodNames.contains(simpleString(method))) continue;

                if(method.getReturnType().getKind() != VOID){
                    def.builder.addMethod(
                        MethodSpec.methodBuilder(var).addModifiers(Modifier.PUBLIC)
                            .returns(TypeName.get(method.getReturnType()))
                            .addAnnotation(cName(Override.class))
                            .addStatement("return $L", var)
                        .build()
                    );
                }

                if(method.getReturnType().getKind() == VOID && !Seq.with(field.annotations).contains(f -> f.type.toString().equals("@unity.annotations.Annotations.ReadOnly"))){
                    def.builder.addMethod(
                        MethodSpec.methodBuilder(var).addModifiers(Modifier.PUBLIC)
                            .returns(TypeName.VOID)
                            .addAnnotation(cName(Override.class))
                            .addParameter(field.type, var)
                            .addStatement("this.$L = $L", var, var)
                        .build()
                    );
                }
            }
        }

        if(def.parent != null){
            def.builder.superclass(cName(Building.class));
            def.parent.builder.addType(def.builder.build());
        }else{
            def.builder.superclass(tName(elements(annotation(def.naming, Merge.class)::base).first()));
        }

        return def.parent == null;
    }

    boolean isCompInterface(TypeElement type){
        return toComp(type) != null;
    }

    String interfaceName(TypeElement type){
        return baseName(type) + "c";
    }

    String baseName(TypeElement type){
        String name = simpleName(type);
        if(!name.endsWith("Comp")){
            throw new IllegalStateException("All types annotated with @MergeComponent must have 'Comp' as the name's suffix: '" + name + "'");
        }

        return name.substring(0, name.length() - 4);
    }

    TypeElement toComp(TypeElement inter){
        String name = simpleName(inter);
        if(!name.endsWith("c")) return null;

        String compName = name.substring(0, name.length() - 1) + "Comp";
        return comps.find(t -> simpleName(t).equals(compName));
    }

    Seq<TypeElement> getDependencies(TypeElement component){
        if(!componentDependencies.containsKey(component)){
            ObjectSet<TypeElement> out = new ObjectSet<>();

            out.addAll(Seq.with(component.getInterfaces())
                .map(BaseProcessor::toEl)
                .<TypeElement>as()
                .map(t -> inters.find(i -> simpleName(t).equals(simpleName(i))))
                .select(Objects::nonNull)
                .map(this::toComp)
            );

            out.remove(component);

            ObjectSet<TypeElement> result = new ObjectSet<>();
            for(TypeElement type : out){
                result.add(type);
                result.addAll(getDependencies(type));
            }

            out.remove(component);
            componentDependencies.put(component, result.asArray());
        }

        return componentDependencies.get(component);
    }

    String createName(Seq<TypeElement> comps){
        Seq<TypeElement> rev = comps.copy();
        rev.reverse();

        return rev.toString("", s -> simpleName(s).replace("Comp", ""));
    }

    boolean append(MethodSpec.Builder mbuilder, Seq<ExecutableElement> values, Seq<ExecutableElement> inserts, boolean writeBlock, ExecutableElement first){
        boolean firstc = true;
        for(ExecutableElement elem : values){
            String descStr = descString(elem);
            String blockName = simpleName(elem.getEnclosingElement()).toLowerCase().replace("comp", "");

            Seq<ExecutableElement> insertComp = inserts.select(e ->
                simpleName(toComp((TypeElement)elements(annotation(e, Insert.class)::block).first()))
                    .toLowerCase().replace("comp", "")
                    .equals(blockName)
            );

            if(is(elem, Modifier.ABSTRACT) || is(elem, Modifier.NATIVE) || (!methodBlocks.containsKey(descStr) && insertComp.isEmpty())) continue;
            if(!firstc) mbuilder.addCode(lnew());
            firstc = false;

            Seq<ExecutableElement> compBefore = insertComp.select(e -> !annotation(e, Insert.class).after());
            compBefore.sort(Structs.comps(Structs.comparingFloat(m -> annotation(m, MethodPriority.class) != null ? annotation(m, MethodPriority.class).value() : 0), Structs.comparing(BaseProcessor::simpleName)));

            Seq<ExecutableElement> compAfter = insertComp.select(e -> annotation(e, Insert.class).after());
            compAfter.sort(Structs.comps(Structs.comparingFloat(m -> annotation(m, MethodPriority.class) != null ? annotation(m, MethodPriority.class).value() : 0), Structs.comparing(BaseProcessor::simpleName)));

            String str = methodBlocks.get(descStr);
            str = str.substring(1, str.length() - 1).trim().replace("\n    ", "\n").trim();
            str += '\n';

            for(ExecutableElement e : compBefore){
                mbuilder.addStatement("this.$L()", simpleName(e));
            }

            if(writeBlock){
                if(annotation(elem, BreakAll.class) == null){
                    str = str.replace("return;", "break " + blockName + ";");
                }

                if(str
                    .replaceAll("\\s+", "")
                    .replace("\n", "")
                    .isEmpty()
                ) continue;

                mbuilder.beginControlFlow("$L:", blockName);
            }

            Seq<String> arguments = new Seq<>();

            Pattern fixer = Pattern.compile("\"\\$.");
            String fixed = new String(str);

            Matcher matcher = fixer.matcher(str);
            while(matcher.find()){
                String snip = matcher.group();
                fixed = fixed.replace(snip, "$L");
                arguments.add(snip);
            }

            mbuilder.addCode(fixed, arguments.toArray(Object.class));

            if(writeBlock) mbuilder.endControlFlow();

            for(ExecutableElement e : compAfter){
                mbuilder.addStatement("this.$L()", simpleName(e));
            }
        }

        return firstc;
    }

    private static class MergeDefinition{
        MergeDefinition parent;
        final Seq<TypeElement> components;
        final Seq<FieldSpec> fieldSpecs;
        final TypeSpec.Builder builder;
        final Element naming;
        final String name;

        MergeDefinition(String name, TypeSpec.Builder builder, Element naming, Seq<TypeElement> components, Seq<FieldSpec> fieldSpec){
            this.builder = builder;
            this.name = name;
            this.naming = naming;
            this.components = components;
            this.fieldSpecs = fieldSpec;
        }

        @Override
        public String toString(){
            return
            "MergeDefinition{" +
                "components=" + components +
                ", base=" + naming +
            '}';
        }
    }
}
