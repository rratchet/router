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
 * 文件名称：PackageUtils.java
 * 文件描述：
 *
 * 创 建 人：ASLai(laijianhua@ruixiude.com)
 *
 * 上次修改时间：2019-07-22 22:39:13
 *
 * 修 改 人：ASLai(laijianhua@ruixiude.com)
 * 修改时间：2019-07-25 14:08:24
 * 修改备注：
 */

package com.rratchet.android.router.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * <pre>
 *
 * 作 者：      ASLai(gdcpljh@126.com).
 * 日 期：      18-7-30
 * 版 本：      V1.0
 * 描 述：      Android package utils
 *
 * </pre>
 *
 * @author ASLai
 */
final class PackageUtils {

    /**
     * Is new version boolean.
     *
     * @param context the context
     *
     * @return the boolean
     */
    public static boolean isNewVersion(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (null != packageInfo) {
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;

            if (!versionName.equals(CacheUtils.getLastVersionName(context))
                    || versionCode != CacheUtils.getLastVersionCode(context)) {
                // new version
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * Update version boolean.
     *
     * @param context the context
     *
     * @return the boolean
     */
    public static boolean updateVersion(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (null != packageInfo) {
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;

            if (!versionName.equals(CacheUtils.getLastVersionName(context))
                    || versionCode != CacheUtils.getLastVersionCode(context)) {
                // new version
                CacheUtils.updateLastVersionCode(context, versionCode);
                CacheUtils.updateLastVersionName(context, versionName);

                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * Gets package info.
     *
     * @param context the context
     *
     * @return the package info
     */
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return packageInfo;
    }
}
