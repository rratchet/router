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
 * 文件名称：BaseProcessor.java
 * 文件描述：
 *
 * 创 建 人：ASLai(laijianhua@ruixiude.com)
 *
 * 上次修改时间：2019-07-23 18:12:21
 *
 * 修 改 人：ASLai(laijianhua@ruixiude.com)
 * 修改时间：2019-07-25 14:08:24
 * 修改备注：
 */

package com.rratchet.android.router.compiler.processor;

import com.rratchet.android.router.compiler.utils.Check;
import com.rratchet.android.router.compiler.utils.Constants;
import com.rratchet.android.router.compiler.utils.Logger;
import com.rratchet.android.router.compiler.utils.TypeUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

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
public abstract class BaseProcessor extends AbstractProcessor
        implements Constants.Options {

    Filer     mFiler;
    Logger    logger;
    Types     types;
    Elements  elementUtils;
    TypeUtils typeUtils;

    /**
     * 模块名
     */
    String  moduleName        = null;
    /**
     * 输出文件的包名
     */
    String  outputPackageName = null;
    /**
     * 是否需要生成doc文档
     */
    boolean generateDoc       = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        mFiler = processingEnv.getFiler();
        types = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        typeUtils = new TypeUtils(types, elementUtils);
        logger = new Logger(processingEnv.getMessager());

        Map<String, String> options = processingEnv.getOptions();
        if (options != null && !options.isEmpty()) {

            moduleName = options.get(KEY_MODULE_NAME);
            generateDoc = VALUE_ENABLE.equalsIgnoreCase(options.get(KEY_GENERATE_DOC));
        }

        if (Check.isEmpty(moduleName)) {
//            moduleName = "Apt";
        }

        if (!Check.isEmpty(moduleName)) {
            moduleName = moduleName.replaceAll("[^0-9a-zA-Z_]+", "");
            outputPackageName = Constants.PackageConfig.GENERATE_TABLES + "." + moduleName.toLowerCase();
            logger.info("The user has configuration the module name, it was [" + moduleName + "]");
        } else {
            logger.error(NO_MODULE_NAME_TIPS);
            throw new RuntimeException("Router::Compiler >>> No module name, for more information, look at gradle log.");
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedOptions() {
        return new HashSet<String>() {{
            this.add(KEY_MODULE_NAME);
            this.add(KEY_GENERATE_DOC);
        }};
    }
}
