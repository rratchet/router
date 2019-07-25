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
 * 文件名称：Constants.java
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

package com.rratchet.android.router.compiler.utils;


/**
 * <pre>
 *
 *      作 者 :        ASLai(laijianhua@ruixiude.com).
 *      日 期 :        2019-07-17
 *      版 本 :        V1.0
 *      描 述 :        description
 *
 *
 * </pre>
 *
 * @author ASLai
 */
public interface Constants {

    interface PackageConfig {
        String ROOT     = "com.rratchet.android.router";
        String TEMPLATE = ROOT + ".template";
        String SERVICE  = ROOT + ".service";
        String MODEL    = ROOT + ".model";

        String GENERATE_TABLES = ROOT + ".tables";
        String GENERATE_DOCS   = ROOT + ".docs";

    }

    interface CustomType {

        String PROVIDER     = PackageConfig.TEMPLATE + ".IProvider";
        String INTERCEPTOR  = PackageConfig.TEMPLATE + ".IInterceptor";
        String TROUTE_ROOT  = PackageConfig.TEMPLATE + ".IRouteRoot";
        String SYRINGE      = PackageConfig.TEMPLATE + ".ISyringe";
        String SON_SERVICE  = PackageConfig.SERVICE + ".SerializationService";
        String TYPE_WRAPPER = PackageConfig.MODEL + ".TypeWrapper";
    }


    interface DefaultConfig {

        String SDK_PACKAGE      = "com.rratchet.android.router";
        String TEMPLATE_PACKAGE = ".template";
        String SERVICE_PACKAGE  = ".service";
        String MODEL_PACKAGE    = ".model";
    }

    interface GenerateConfig {

        String SEPARATOR           = "$$";
        String PROJECT             = "Router";
        String TAG                 = PROJECT + "::";
        String WARNING_TIPS        = "DO NOT EDIT THIS FILE!!! IT WAS GENERATED BY ROUTER.\n";
        String METHOD_ROUTE_TYPE   = "routeType";
        String METHOD_LOAD_INTO    = "loadInto";
        String METHOD_BOOTSTRAP    = "bootstrap";
        String METHOD_INJECT       = "inject";
        String NAME_OF_ROOT        = PROJECT + SEPARATOR + "Root";
        String NAME_OF_PROVIDER    = PROJECT + SEPARATOR + "Providers";
        String NAME_OF_INTERCEPTOR = PROJECT + SEPARATOR + "Interceptors";
        String NAME_OF_REQUESTED   = SEPARATOR + PROJECT + SEPARATOR + "Requested";
    }

    interface AndroidType {
        String ACTIVITY    = "android.app.Activity";
        String FRAGMENT    = "android.app.Fragment";
        String FRAGMENT_V4 = "android.support.v4.app.Fragment";
        String FRAGMENT_X  = "androidx.fragment.app.Fragment";
        String SERVICE     = "android.app.Service";
        String PARCELABLE  = "android.os.Parcelable";
    }

    interface JavaType {
        String BYTE         = "java.lang.Byte";
        String SHORT        = "java.lang.Short";
        String INTEGER      = "java.lang.Integer";
        String LONG         = "java.lang.Long";
        String FLOAT        = "java.lang.Float";
        String DOUBLE       = "java.lang.Double";
        String BOOLEAN      = "java.lang.Boolean";
        String CHAR         = "java.lang.Character";
        String STRING       = "java.lang.String";
        String SERIALIZABLE = "java.io.Serializable";
    }

    interface LogConfig {
        String PREFIX_OF_LOGGER = GenerateConfig.PROJECT + "::Compiler ";

    }

    interface Options {
        String KEY_MODULE_NAME  = "routerModuleName";
        String KEY_GENERATE_DOC = "routerGenerateDoc";
        String VALUE_ENABLE     = "enable";


        /**
         * gradle 未配置 MODULE_NAME 时的提示
         */
        String NO_MODULE_NAME_TIPS = "These no module name, at 'build.gradle', like :\n" +
                "android {\n" +
                "    defaultConfig {\n" +
                "        ...\n" +
                "        javaCompileOptions {\n" +
                "            annotationProcessorOptions {\n" +
                "                arguments = [" +
                "                   routerModuleName: project.getName()" +
                "               ]\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}\n";
    }

    interface AnnotationType {
        String ANNOTATION_TYPE_INTECEPTOR = "com.rratchet.android.router.support.annotation.Interceptor";
        String ANNOTATION_TYPE_ROUTE      = "com.rratchet.android.router.support.annotation.Route";
        String ANNOTATION_TYPE_REQUESTED  = "com.rratchet.android.router.support.annotation.Requested";
    }


}
