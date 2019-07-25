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
 * 文件名称：Router.java
 * 文件描述：
 *
 * 创 建 人：ASLai(laijianhua@ruixiude.com)
 *
 * 上次修改时间：2019-07-25 12:17:17
 *
 * 修 改 人：ASLai(laijianhua@ruixiude.com)
 * 修改时间：2019-07-25 14:08:24
 * 修改备注：
 */

package com.rratchet.android.router;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.rratchet.android.router.support.enums.RouteType;
import com.rratchet.android.router.support.model.Attribute;
import com.rratchet.android.router.template.IRouteBootstrapper;

import java.util.Map;

/**
 * <pre>
 *
 *      作 者 :        ASLai(laijianhua@ruixiude.com).
 *      日 期 :        2019-07-22
 *      版 本 :        V1.0
 *      描 述 :        Android 路由
 *
 *
 * </pre>
 *
 * @author ASLai
 */
public final class Router {

    public static final String TAG = Router.class.getSimpleName();

    /**
     * The constant sDebuggable.
     */
    private static boolean sDebuggable = false;

    private Router() {
    }

    /**
     * Debuggable boolean.
     *
     * @return the boolean
     */
    public static boolean debuggable() {
        return sDebuggable;
    }

    /**
     * Open debug.
     */
    public static void openDebug() {
        sDebuggable = true;
    }

    /**
     * Close debug.
     */
    public static void closeDebug() {
        sDebuggable = false;
    }

    /**
     * Is app in debug boolean.
     *
     * @param context the context
     * @return the boolean
     */
    public static boolean isAppInDebug(Context context) {
        boolean debuggable = true;
        try {
            ApplicationInfo info = context.getApplicationInfo();
            debuggable = (0 != (info.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (Exception e) {

        }
        return debuggable;
    }

    /**
     * 初始化服务管理类，在使用该类时确保先进行初始化
     *
     * @param application the application
     */
    public static void init(Application application) {
        _Router.getInstance().init(application);
    }

    /**
     * 对外提供手动注册方法
     *
     * @param target the target
     */
    public static void register(@NonNull Class<? extends IRouteBootstrapper> target) {
        _Router.getInstance().register(target);
    }

    public static void inject(Object target) {
        _Router.getInstance().inject(target);
    }

    public static void inject(Object target, Intent intent) {
        _Router.getInstance().inject(target, intent);
    }

    private static Context context() {
        return _Router.getInstance().mContext;
    }

    private static Map<RouteType, Map<String, Attribute>> configs() {
        return _Router.getInstance()._configs;
    }

    public static RouteInfo activity(String path) {
        return _Router.getInstance().build(RouteType.ACTIVITY, path);
    }

    public static RouteInfo fragment(String path) {
        return _Router.getInstance().build(RouteType.FRAGMENT, path);
    }

    public static RouteInfo service(String path) {
        return _Router.getInstance().build(RouteType.SERVICE, path);
    }

    public static RouteInfo provider(String path) {
        return _Router.getInstance().build(RouteType.PROVIDER, path);
    }

    public static RouteInfo activity(Uri uri) {
        return _Router.getInstance().build(RouteType.ACTIVITY, uri);
    }

    public static RouteInfo fragment(Uri uri) {
        return _Router.getInstance().build(RouteType.FRAGMENT, uri);
    }

    public static RouteInfo service(Uri uri) {
        return _Router.getInstance().build(RouteType.SERVICE, uri);
    }

    public static RouteInfo provider(Uri uri) {
        return _Router.getInstance().build(RouteType.PROVIDER, uri);
    }

    static void start(Context context, RouteInfo routeInfo) {
        _Router.getInstance().start(context, routeInfo);
    }

    static Object get(RouteInfo routeInfo) {
        return _Router.getInstance().get(routeInfo);
    }

}
