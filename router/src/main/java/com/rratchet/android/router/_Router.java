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
 * 文件名称：_Router.java
 * 文件描述：
 *
 * 创 建 人：ASLai(laijianhua@ruixiude.com)
 *
 * 上次修改时间：2019-07-25 12:13:35
 *
 * 修 改 人：ASLai(laijianhua@ruixiude.com)
 * 修改时间：2019-07-25 14:08:24
 * 修改备注：
 */

package com.rratchet.android.router;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.rratchet.android.router.core.RouterDelegate;
import com.rratchet.android.router.support.enums.RouteType;
import com.rratchet.android.router.support.model.Attribute;
import com.rratchet.android.router.template.IRouteBootstrapper;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * <pre>
 *
 *      作 者 :        ASLai(laijianhua@ruixiude.com).
 *      日 期 :        2019-07-19
 *      版 本 :        V1.0
 *      描 述 :        description
 *
 *
 * </pre>
 *
 * @author ASLai
 */
final class _Router {

    /**
     * The Configs.
     */
    final Map<RouteType, Map<String, Attribute>> _configs       = new TreeMap<>();
    /**
     * The Delegate class.
     */
    final RouterDelegate                  mRouterDelegate;
    /**
     * The constant mContext.
     */
    Context mContext;


    /**
     * Instantiates a new Router.
     */
    _Router() {

        for (RouteType value : RouteType.values()) {
            Map<String, Attribute> table = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    try {
                        return o1.compareTo(o2);
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                        return 0;
                    }
                }
            });
            _configs.put(value, table);
        }

        RouterDelegate routerDelegate = null;
        try {
            Method instance = RouterDelegate.class.getDeclaredMethod("getInstance");
            instance.setAccessible(true);
            routerDelegate = (RouterDelegate) instance.invoke(RouterDelegate.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRouterDelegate = routerDelegate;
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static _Router getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Init.
     *
     * @param application the application
     */
    public synchronized void init(Application application) {
        mContext = application;

        _init();
    }

    /**
     * Init.
     */
    private synchronized void _init() {

        try {
            mRouterDelegate.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Register.
     *
     * @param target the target
     */
    public void register(@NonNull Class<? extends IRouteBootstrapper> target) {

        try {
            IRouteBootstrapper bootstrapper = target.getConstructor().newInstance();
            if (bootstrapper != null) {
                bootstrapper.bootstrap(_configs);
            } else {
                RuntimeException exception = new RuntimeException(target.getSimpleName() + " constructor cannot be found!");
                exception.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inject.
     *
     * @param target the target
     */
    public void inject(@NonNull Object target) {
        try {
            mRouterDelegate.inject(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inject.
     *
     * @param target the target
     * @param intent the intent
     */
    public void inject(@NonNull Object target, @NonNull Intent intent) {
        try {
            mRouterDelegate.inject(target, new RouteBundle(intent.getExtras(), intent.getData()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Build variable.
     *
     * @param routeType the route type
     * @param uri       the uri
     * @return the variable
     */
    public RouteInfo build(RouteType routeType, Uri uri) {

        String path;
        if (uri == null || TextUtils.isEmpty(uri.getPath())) {
            path = "";
        } else {
            path = uri.getPath();
        }

        return build(routeType, path);
    }

    /**
     * Build variable.
     *
     * @param routeType the route type
     * @param path      the path
     * @return the variable
     */
    public RouteInfo build(RouteType routeType, String path) {

        RouteInfo routeInfo = new RouteInfo();

        Attribute attribute = extract(routeType, path);

        if (attribute == null) {
            return routeInfo;
        }

        routeInfo.setAttribute(attribute);

        return routeInfo;
    }

    public void start(Context context, RouteInfo routeInfo) {

        try {
            mRouterDelegate.start(context == null ? mContext : context, routeInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object get(RouteInfo routeInfo) {

        try {
            return mRouterDelegate.get(routeInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从配置中提取相应的属性
     *
     * @param routeType the route type
     * @param path      the path
     * @return attribute
     */
    private Attribute extract(RouteType routeType, String path) {
        Map<String, Attribute> table = _configs.get(routeType);
        if (table == null) {
            return null;
        }
        Attribute attribute = table.get(path == null ? "" : path);
        return attribute;
    }

    /**
     * The type Singleton holder.
     */
    private static class SingletonHolder {
        /**
         * The constant INSTANCE.
         */
        private static final _Router INSTANCE = new _Router();
    }
}
