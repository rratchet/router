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
 * 文件名称：Logger.java
 * 文件描述：
 *
 * 创 建 人：ASLai(laijianhua@ruixiude.com)
 *
 * 上次修改时间：2019-07-22 22:28:20
 *
 * 修 改 人：ASLai(laijianhua@ruixiude.com)
 * 修改时间：2019-07-25 14:08:24
 * 修改备注：
 */

package com.rratchet.android.router.compiler.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * <pre>
 *
 *      作 者 :        ASLai(laijianhua@ruixiude.com).
 *      日 期 :        2019-07-17
 *      版 本 :        V1.0
 *      描 述 :        日志打印器
 *
 *
 * </pre>
 *
 * @author ASLai
 */
public class Logger {

    /**
     * The Messager.
     */
    private Messager messager;

    /**
     * Instantiates a new Logger.
     *
     * @param messager the messager
     */
    public Logger(Messager messager) {
        this.messager = messager;
    }

    /**
     * Is not empty boolean.
     *
     * @param text the text
     * @return the boolean
     */
    private static boolean isNotEmpty(CharSequence text) {
        return text != null && text.length() > 0;
    }

    /**
     * Info.
     *
     * @param info the info
     */
    public void info(CharSequence info) {
        if (isNotEmpty(info)) {
            messager.printMessage(Diagnostic.Kind.NOTE, Constants.LogConfig.PREFIX_OF_LOGGER + info);
        }
    }

    /**
     * Error.
     *
     * @param error the error
     */
    public void error(CharSequence error) {
        if (isNotEmpty(error)) {
            messager.printMessage(Diagnostic.Kind.ERROR, Constants.LogConfig.PREFIX_OF_LOGGER + "An exception is encountered, [" + error + "]");
        }
    }

    /**
     * Error.
     *
     * @param error the error
     */
    public void error(Throwable error) {
        if (null != error) {
            messager.printMessage(Diagnostic.Kind.ERROR, Constants.LogConfig.PREFIX_OF_LOGGER + "An exception is encountered, [" + error.getMessage() + "]" + "\n" + formatStackTrace(error.getStackTrace()));
        }
    }

    /**
     * Warning.
     *
     * @param warning the warning
     */
    public void warning(CharSequence warning) {
        if (isNotEmpty(warning)) {
            messager.printMessage(Diagnostic.Kind.WARNING, Constants.LogConfig.PREFIX_OF_LOGGER + warning);
        }
    }

    /**
     * Format stack trace string.
     *
     * @param stackTrace the stack trace
     * @return the string
     */
    private String formatStackTrace(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            sb.append("    at ").append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

}
