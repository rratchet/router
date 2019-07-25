/*
 * Copyright (c) 2019. RRatChet Open Source Project.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 项目名称：rratchet-android-router-trunk
 * 模块名称：router-compiler
 *
 * 文件名称：RouterProcessor.java
 * 文件描述：
 *
 * 创 建 人：ASLai(laijianhua@ruixiude.com)
 *
 * 上次修改时间：2019-07-23 15:40:31
 *
 * 修 改 人：ASLai(laijianhua@ruixiude.com)
 * 修改时间：2019-07-25 14:08:24
 * 修改备注：
 */

package com.rratchet.android.router.compiler.processor;

import com.google.auto.service.AutoService;
import com.rratchet.android.router.compiler.utils.Check;
import com.rratchet.android.router.compiler.utils.Constants.GenerateConfig;
import com.rratchet.android.router.support.annotation.Requested;
import com.rratchet.android.router.support.annotation.Route;
import com.rratchet.android.router.support.enums.FieldType;
import com.rratchet.android.router.support.enums.RouteType;
import com.rratchet.android.router.support.model.Attribute;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.StandardLocation;

import static com.rratchet.android.router.compiler.utils.Constants.AndroidType;
import static com.rratchet.android.router.compiler.utils.Constants.AnnotationType;
import static com.rratchet.android.router.compiler.utils.Constants.CustomType;
import static com.rratchet.android.router.compiler.utils.Constants.PackageConfig;

/**
 * <pre>
 *
 *      作 者 :        ASLai(laijianhua@ruixiude.com).
 *      日 期 :        2019-07-18
 *      版 本 :        V1.0
 *      描 述 :        description
 *
 *
 * </pre>
 *
 * @author ASLai
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({AnnotationType.ANNOTATION_TYPE_ROUTE, AnnotationType.ANNOTATION_TYPE_REQUESTED})
public class RouterProcessor extends BaseProcessor {

    /**
     * The Configs.
     */
    private Map<String, Set<Attribute>> configs = new TreeMap<>();

    /**
     * 文档写入器
     */
    private Writer docWriter;

    /**
     * Verify attribute boolean.
     *
     * @param attribute the attribute
     * @return the boolean
     */
    private static final boolean verifyAttribute(Attribute attribute) {

        if (attribute == null) {
            return false;
        }

        String path = attribute.getPath();

        // path 必须以 '/' 开头并且不能为空
        if (Check.isEmpty(path) || !path.startsWith("/")) {
            return false;
        }

        return true;
    }

    /**
     * Init.
     *
     * @param processingEnv the processing env
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        if (generateDoc) {
            try {
                docWriter = mFiler.createResource(
                        StandardLocation.SOURCE_OUTPUT,
                        PackageConfig.GENERATE_DOCS,
                        "router-map-of-" + moduleName + ".json"
                ).openWriter();
            } catch (IOException e) {
                logger.error("Create doc writer failed, because " + e.getMessage());
            }
        }

        logger.info(">>> RouterProcessor init. <<<");
    }

    /**
     * Gets supported annotation types.
     *
     * @return the supported annotation types
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    /**
     * {@inheritDoc}
     *
     * @param annotations the annotations
     * @param roundEnv    the round env
     * @return the boolean
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (!Check.isEmpty(annotations)) {

            Set<? extends Element> routeElements = roundEnv.getElementsAnnotatedWith(Route.class);

            try {
                // 开始解析注解
                logger.info(">>> Found routes, start... <<<");
                parseRoutes(routeElements);
            } catch (Exception e) {
                logger.error(e);
            }
            return true;
        }

        return false;
    }

    /**
     * Parse routes.
     *
     * @param elements the elements
     * @throws Exception the exception
     */
    private void parseRoutes(Set<? extends Element> elements) throws Exception {

        if (Check.isEmpty(elements)) {
            return;
        }

        logger.info(">>> Found routes, size is " + elements.size() + " <<<");

        final TypeMirror typeOfActivity = elementUtils.getTypeElement(AndroidType.ACTIVITY).asType();
        final TypeMirror typeOfService = elementUtils.getTypeElement(AndroidType.SERVICE).asType();
        final TypeMirror typeOfFragment = elementUtils.getTypeElement(AndroidType.FRAGMENT).asType();
        final TypeMirror typeOfFragmentV4 = elementUtils.getTypeElement(AndroidType.FRAGMENT_V4).asType();
        final TypeMirror typeOfFragmentX = elementUtils.getTypeElement(AndroidType.FRAGMENT_X).asType();

        final TypeMirror typeOfProvider = elementUtils.getTypeElement(CustomType.PROVIDER).asType();

        for (Element element : elements) {

            TypeMirror type = element.asType();
            Route route = element.getAnnotation(Route.class);

            Attribute.Builder builder = new Attribute.Builder()
                    .withPath(route.path())
                    .withAlias(route.alias())
                    .withRemark(route.remark())
                    .withPriority(route.priority())
                    .withRawType(element);


            if (types.isSubtype(type, typeOfActivity)) {

                Map<String, Requested> injectFields = new HashMap<>();
                Map<String, FieldType> fieldTypes = new HashMap<>();

                for (Element member : element.getEnclosedElements()) {
                    if (member.getKind().isField() && !types.isSubtype(member.asType(), typeOfProvider)) {
                        // 必须是字段且不能实现 provider
                        Requested required = member.getAnnotation(Requested.class);
                        if (required != null) {
                            // 存在 Requested 注解
                            String injectName = Check.isEmpty(required.name()) ? member.getSimpleName().toString() : required.name();

                            injectFields.put(injectName, required);
                            FieldType fieldType = typeUtils.typeExchange(member);
                            fieldTypes.put(injectName, fieldType);
                        }
                    }
                }

                builder.withRouteType(RouteType.ACTIVITY);
                builder.withInjectFields(injectFields);
                builder.withFieldTypes(fieldTypes);

            } else if (types.isSubtype(type, typeOfService)) {
                builder.withRouteType(RouteType.SERVICE);
            } else if (types.isSubtype(type, typeOfFragment)) {
                builder.withRouteType(RouteType.FRAGMENT);
            } else if (types.isSubtype(type, typeOfFragmentV4)) {
                builder.withRouteType(RouteType.FRAGMENT);
            } else if (types.isSubtype(type, typeOfFragmentX)) {
                builder.withRouteType(RouteType.FRAGMENT);
            } else if (types.isSubtype(type, typeOfProvider)) {
                builder.withRouteType(RouteType.PROVIDER);
            } else {
                throw new RuntimeException("Router::Compiler >>> Found unsupported class type, type = [" + types.toString() + "].");
            }


            Attribute attribute = builder.build();

            classify(attribute);
        }


        Set<Attribute> activities = configs.get(RouteType.ACTIVITY.name());
        Set<Attribute> services = configs.get(RouteType.SERVICE.name());
        Set<Attribute> fragments = configs.get(RouteType.FRAGMENT.name());
        Set<Attribute> providers = configs.get(RouteType.PROVIDER.name());

        Set<ClassName> tables = new TreeSet<>();

        if (!Check.isEmpty(activities)) {

            ClassName className = ClassName.get(outputPackageName, "ActivityRouteTableImpl");
            if (generateRouteTable(RouteType.ACTIVITY, className, activities)) {
                tables.add(className);
            }
        }

        if (!Check.isEmpty(services)) {

            ClassName className = ClassName.get(outputPackageName, "ServiceRouteTableImpl");
            if (generateRouteTable(RouteType.SERVICE, className, services)) {
                tables.add(className);
            }
        }

        if (!Check.isEmpty(fragments)) {

            ClassName className = ClassName.get(outputPackageName, "FragmentRouteTableImpl");
            if (generateRouteTable(RouteType.FRAGMENT, className, fragments)) {
                tables.add(className);
            }
        }

        if (!Check.isEmpty(providers)) {

            ClassName className = ClassName.get(outputPackageName, "ProviderRouteTableImpl");
            if (generateRouteTable(RouteType.PROVIDER, className, providers)) {
                tables.add(className);
            }
        }

        for (RouteType value : RouteType.values()) {
            String routeType = value.name();
            Set<Attribute> attributes = configs.get(routeType);
            if (Check.isEmpty(attributes)) {
                continue;
            }

            for (Attribute attribute : attributes) {

            }
        }

        if (!Check.isEmpty(tables)) {
            // 创建RouteBootstrapper，首字母大写
            String moduleName = this.moduleName.substring(0, 1).toUpperCase() + this.moduleName.substring(1);
            ClassName className = ClassName.get(outputPackageName, moduleName + "RouteBootstrapper");
            generateBootstrapper(className, tables);
        }

    }

    /**
     * 路由分类
     *
     * @param attribute the attribute
     */
    private void classify(Attribute attribute) {

        if (verifyAttribute(attribute)) {

            RouteType routeType = attribute.getRouteType();
            logger.info(">>> Start classification, type = " + routeType.name() + ", path = " + attribute.getPath() + " <<<");

            Set<Attribute> routeTable = configs.get(routeType.name());
            if (routeTable == null) {
                routeTable = new TreeSet<>(new Comparator<Attribute>() {
                    @Override
                    public int compare(Attribute o1, Attribute o2) {
                        try {
                            return o1.getPath().compareTo(o2.getPath());
                        } catch (NullPointerException npe) {
                            logger.error(npe.getMessage());
                            return 0;
                        }
                    }
                });
            }
            routeTable.add(attribute);
            configs.put(routeType.name(), routeTable);
        } else {
            logger.warning(">>> Route attribute verify error, type is " + attribute.getRawType().getSimpleName() + " <<<");
        }
    }

    /**
     * 生成同一路由类型的路由表
     *
     * @param routeType  the route type
     * @param className  the class name
     * @param attributes the attributes
     * @return boolean
     */
    private boolean generateRouteTable(RouteType routeType, ClassName className, Set<Attribute> attributes) {

        if (Check.isEmpty(attributes)) {
            return false;
        }

        /*
         *
         * 开始生成相应代码文件
         *
         * final class XXXRouteTable implements IRouteTable {
         *
         *     @Override
         *     public void loadInto(Map<String, Attribute> table) {
         *
         *     }
         *
         * }
         *
         */

        MethodSpec.Builder routeTypeMethodBuilder = null;
        MethodSpec.Builder loadIntoMethodBuilder = null;

        ClassName iRouteTable = ClassName.get(PackageConfig.TEMPLATE, "IRouteTable");

        TypeElement typeElement = elementUtils.getTypeElement(iRouteTable.toString());
        List<? extends Element> members = elementUtils.getAllMembers(typeElement);
        for (Element member : members) {

            String methodName = member.getSimpleName().toString();
            if (GenerateConfig.METHOD_ROUTE_TYPE.equals(methodName)) {
                routeTypeMethodBuilder = MethodSpec.overriding((ExecutableElement) member);
                continue;
            }
            if (GenerateConfig.METHOD_LOAD_INTO.equals(methodName)) {
                loadIntoMethodBuilder = MethodSpec.overriding((ExecutableElement) member);
                continue;
            }
        }

        if (routeTypeMethodBuilder == null) {
            return false;
        }

        if (loadIntoMethodBuilder == null) {
            return false;
        }

        logger.info(">>> Generate table[" + routeType.name() + "], start... <<<");

        routeTypeMethodBuilder.addStatement(
                "return $T." + routeType.name(),
                ClassName.get(RouteType.class)
        );

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className)
                .addSuperinterface(iRouteTable)
                .addModifiers(Modifier.FINAL);

        int attributeIndex = 0;

        loadIntoMethodBuilder.addStatement("$T temp = null", ClassName.get(Attribute.class));

        for (Attribute attribute : attributes) {

            String path = attribute.getPath();

            ClassName target = ClassName.get((TypeElement) attribute.getRawType());

            String attrName = "attribute" + (attributeIndex++);
            loadIntoMethodBuilder.addStatement(
                    "$T $L = " + "$T.build(" +
                            "$T.$L," +
                            "$T.class," +
                            "$S," +
                            "$S," +
                            "$S," +
                            attribute.getPriority() +
                            ")",
                    ClassName.get(Attribute.class),
                    attrName,
                    ClassName.get(Attribute.class),
                    ClassName.get(RouteType.class),
                    attribute.getRouteType().name(),
                    target,
                    attribute.getPath(),
                    attribute.getAlias(),
                    attribute.getRemark()
            );

            loadIntoMethodBuilder.addStatement("temp = arg0.get($S)", path);

            // 增加优先级判断，优先级越高，数值越低
            CodeBlock codeBlock = CodeBlock.builder()
                    .add("if (temp == null) {").add("\n")
                    .indent()
                    .add("arg0.put($S, $L)", path, attrName).add(";\n")
                    .unindent()
                    .add("} else if ($L.getPriority() < temp.getPriority()) {", attrName).add("\n")
                    .indent()
                    .add("arg0.put($S, $L)", path, attrName).add(";\n")
                    .unindent()
                    .add("}").add("\n")
                    .build();

            loadIntoMethodBuilder.addCode(codeBlock);

        }

        typeBuilder.addMethod(routeTypeMethodBuilder.build());
        typeBuilder.addMethod(loadIntoMethodBuilder.build());

        typeBuilder.addJavadoc(
                CodeBlock.builder()
                        .add(GenerateConfig.WARNING_TIPS)
                        .build()
        );

        JavaFile.Builder builder = JavaFile.builder(outputPackageName, typeBuilder.build());

        JavaFile javaFile = builder.build();

        try {
            javaFile.writeTo(mFiler);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

    /**
     * 生成路由启动加载器
     *
     * @param className the class name
     * @param tables    the tables
     * @return boolean
     */
    private boolean generateBootstrapper(ClassName className, Set<ClassName> tables) {

        if (Check.isEmpty(tables)) {
            return false;
        }

        /*
         *
         * 开始生成相应代码文件
         *
         * public class XXXRouteBootstrapperImpl implements IRouteBootstrapper {
         *
         *     @Override
         *     public void bootstrap(Map<RouteType, Map<String, Attribute>> config) {
         *
         *     }
         *
         * }
         *
         */

        MethodSpec.Builder methodBuilder = null;

        ClassName iRouteBootstrapper = ClassName.get(PackageConfig.TEMPLATE, "IRouteBootstrapper");

        TypeElement typeElement = elementUtils.getTypeElement(iRouteBootstrapper.toString());
        List<? extends Element> members = elementUtils.getAllMembers(typeElement);
        for (Element member : members) {

            if (GenerateConfig.METHOD_BOOTSTRAP.equals(member.getSimpleName().toString())) {
                methodBuilder = MethodSpec.overriding((ExecutableElement) member);
            }
        }

        if (methodBuilder == null) {
            return false;
        }

        logger.info(">>> Generate Bootstrapper[" + className.simpleName() + "], start... <<<");

        int argIndex = 0;
        for (ClassName table : tables) {
            String name = "table" + argIndex++;
            methodBuilder.addStatement(
                    "$T $L = new $T()",
                    table,
                    name,
                    table
            );
            methodBuilder.addStatement(
                    "$L.loadInto(arg0.get($L.routeType()))",
                    name,
                    name
            );
        }

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className)
                .addSuperinterface(iRouteBootstrapper)
                .addModifiers(Modifier.PUBLIC);

        typeBuilder.addMethod(methodBuilder.build());

        typeBuilder.addJavadoc(
                CodeBlock.builder()
                        .add(GenerateConfig.WARNING_TIPS)
                        .build()
        );

        JavaFile.Builder builder = JavaFile.builder(outputPackageName, typeBuilder.build());

        JavaFile javaFile = builder.build();

        try {
            javaFile.writeTo(mFiler);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

}
