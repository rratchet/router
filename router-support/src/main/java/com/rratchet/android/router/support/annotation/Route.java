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
 * 模块名称：router-support
 *
 * 文件名称：Route.java
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

package com.rratchet.android.router.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *
 *      作 者 :        ASLai(laijianhua@ruixiude.com).
 *      日 期 :        2019-07-17
 *      版 本 :        V1.0
 *      描 述 :        用于标记一个具备路由的界面
 *
 *
 * </pre>
 *
 * @author ASLai
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface Route {


    /**
     * 路由的路径.
     *
     * @return the string
     */
    String path();

    /**
     * 别名.
     *
     * @return the string
     */
    String alias() default "";

    /**
     * 备注.
     *
     * @return
     */
    String remark() default "";

    /**
     * 优先级.
     *
     * @return the int
     */
    int priority() default -1;
}
