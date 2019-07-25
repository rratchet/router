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
 * 模块名称：router
 *
 * 文件名称：CacheUtils.java
 * 文件描述：
 *
 * 创 建 人：ASLai(laijianhua@ruixiude.com)
 *
 * 上次修改时间：2019-07-22 22:50:06
 *
 * 修 改 人：ASLai(laijianhua@ruixiude.com)
 * 修改时间：2019-07-25 14:08:24
 * 修改备注：
 */

package com.rratchet.android.router.core;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.Set;

/**
 * <pre>
 *
 * 作 者：      ASLai(gdcpljh@126.com).
 * 日 期：      18-7-30
 * 版 本：      V1.0
 * 描 述：      组件相关的缓存工具
 *
 * </pre>
 *
 * @author ASLai
 */
final class CacheUtils {

    /**
     * 启动器的类集合的缓存key
     */
    static final         String KEY_BOOTSTRAPPER_CLASS_SET = "bootstrapperClassSet";
    /**
     * apk缓存的版本号key
     */
    static final         String KEY_LAST_VERSION_CODE      = "lastVersionCode";
    /**
     * The Key last version name.
     */
    static final         String KEY_LAST_VERSION_NAME      = "lastVersionName";
    /**
     * The constant NAME.
     */
    private static final String NAME                       = "android_router_cache";

    /**
     * Gets preferences.
     *
     * @param context the context
     * @return the preferences
     */
    static SharedPreferences getPreferences(@NonNull Context context) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    /**
     * Gets boot strapper class set.
     *
     * @param context the context
     * @return the bootstrapper class set
     */
    public static Set<String> getBootstrapper(@NonNull Context context) {
        Set<String> set = getPreferences(context).getStringSet(KEY_BOOTSTRAPPER_CLASS_SET, null);
        return set;
    }

    /**
     * Update bootstrapper class set.
     *
     * @param context              the context
     * @param bootstrappers the bootstrapper class set
     */
    public static void updateBootstrapper(@NonNull Context context, Set<String> bootstrappers) {
        getPreferences(context).edit().putStringSet(KEY_BOOTSTRAPPER_CLASS_SET, bootstrappers);
    }

    /**
     * Gets last version code.
     *
     * @param context the context
     * @return the last version code
     */
    public static int getLastVersionCode(@NonNull Context context) {
        return getPreferences(context).getInt(KEY_LAST_VERSION_CODE, -1);
    }

    /**
     * Update last version code.
     *
     * @param context     the context
     * @param versionCode the version code
     */
    public static void updateLastVersionCode(@NonNull Context context, int versionCode) {
        getPreferences(context).edit().putInt(KEY_LAST_VERSION_CODE, versionCode).apply();
    }

    /**
     * Gets last version name.
     *
     * @param context the context
     * @return the last version name
     */
    public static String getLastVersionName(@NonNull Context context) {
        return getPreferences(context).getString(KEY_LAST_VERSION_NAME, null);
    }

    /**
     * Update last version name.
     *
     * @param context     the context
     * @param versionName the version name
     */
    public static void updateLastVersionName(@NonNull Context context, String versionName) {
        getPreferences(context).edit().putString(KEY_LAST_VERSION_NAME, versionName).apply();
    }
}
