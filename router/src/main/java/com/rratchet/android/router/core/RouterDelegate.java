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
 * 文件名称：RouterDelegate.java
 * 文件描述：
 *
 * 创 建 人：ASLai(laijianhua@ruixiude.com)
 *
 * 上次修改时间：2019-07-25 13:52:51
 *
 * 修 改 人：ASLai(laijianhua@ruixiude.com)
 * 修改时间：2019-07-25 14:08:24
 * 修改备注：
 */

package com.rratchet.android.router.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.util.LruCache;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.rratchet.android.router.RouteBundle;
import com.rratchet.android.router.RouteInfo;
import com.rratchet.android.router.Router;
import com.rratchet.android.router.support.annotation.Requested;
import com.rratchet.android.router.support.enums.RouteType;
import com.rratchet.android.router.support.model.Attribute;
import com.rratchet.android.router.template.IProvider;
import com.rratchet.android.router.template.IRouteBootstrapper;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *
 *      作 者 :        ASLai(laijianhua@ruixiude.com).
 *      日 期 :        2019-07-24
 *      版 本 :        V1.0
 *      描 述 :        代理类
 *
 *
 * </pre>
 *
 * @author ASLai
 */
public final class RouterDelegate {

    private static volatile RouterDelegate             mInstance;
    /**
     * The Providers cache.
     */
    final                   LruCache<String, Class<?>> providersCache = new LruCache<>(66);
    volatile                Handler                    mHandler;

    /**
     * Instantiates a new Router delegate.
     */
    private RouterDelegate() {
    }

    private static RouterDelegate getInstance() {
        if (mInstance == null) {
            synchronized (RouterDelegate.class) {
                if (mInstance == null) {
                    mInstance = new RouterDelegate();
                }
            }
        }
        return mInstance;
    }

    /**
     * 过滤验证加载程序集
     *
     * @param classNames the class names
     * @return set set
     */
    private static Set<String> filterVerify(Set<String> classNames) {

        final Set<String> set = new HashSet<>();
        if (classNames != null && !classNames.isEmpty()) {

            String prefix = Constants.PACKAGE_NAME_TABLES;
            String suffix = Constants.SUFFIX_OF_BOOTSTRAPPER;

            for (String className : classNames) {
                if (className.startsWith(prefix) && className.endsWith(suffix)) {
                    set.add(className);
                }
            }
        }
        return set;
    }

    /**
     * 获取参数对象
     *
     * @param target the target
     * @return route bundle
     * @throws Exception the exception
     */
    private static RouteBundle obtainBundle(Object target) throws Exception {

        Class<?> targetClass = target.getClass();

        Class<?> activityClass = Class.forName("android.app.Activity");
        Class<?> fragmentClass = Class.forName("android.app.Fragment");
        Class<?> fragmentV4Class = Class.forName("android.support.v4.app.Fragment");
        Class<?> fragmentXClass = Class.forName("androidx.fragment.app.Fragment");

        //        Activity activity = new Activity();
        //        Intent intent = activity.getIntent();
        //        Bundle bundle = intent.getExtras();
        //        Uri uri = intent.getData();
        //        Fragment fragment = new Fragment();
        //        fragment.getActivity().getIntent().getExtras();

        if (activityClass.isAssignableFrom(targetClass)) {

            Method method = targetClass.getMethod("getIntent");
            Intent intent = (Intent) method.invoke(target);
            Bundle bundle = intent.getExtras();
            Uri uri = intent.getData();
            return new RouteBundle(bundle, uri);

        } else if (fragmentClass.isAssignableFrom(targetClass)) {

            Method method = targetClass.getMethod("getActivity");
            Activity activity = (Activity) method.invoke(target);
            Intent intent = activity.getIntent();
            Bundle bundle = intent.getExtras();
            Uri uri = intent.getData();
            return new RouteBundle(bundle, uri);

        } else if (fragmentV4Class.isAssignableFrom(targetClass)) {

            Method method = targetClass.getMethod("getActivity");
            Activity activity = (Activity) method.invoke(target);
            Intent intent = activity.getIntent();
            Bundle bundle = intent.getExtras();
            Uri uri = intent.getData();
            return new RouteBundle(bundle, uri);

        } else if (fragmentXClass.isAssignableFrom(targetClass)) {

            Method method = targetClass.getMethod("getActivity");
            Activity activity = (Activity) method.invoke(target);
            Intent intent = activity.getIntent();
            Bundle bundle = intent.getExtras();
            Uri uri = intent.getData();
            return new RouteBundle(bundle, uri);

        }

        return null;

    }

    /**
     * 获取Context
     *
     * @param target
     * @return
     * @throws Exception
     */
    private static Context obtainContext(Object target) throws Exception {

        if (target instanceof Context) {
            return (Context) target;
        }

        Class<?> targetClass = target.getClass();

        Class<?> fragmentClass = Class.forName("android.app.Fragment");
        Class<?> fragmentV4Class = Class.forName("android.support.v4.app.Fragment");
        Class<?> fragmentXClass = Class.forName("androidx.fragment.app.Fragment");

        if (fragmentClass.isAssignableFrom(targetClass)) {

            Method method = targetClass.getMethod("getActivity");
            Activity activity = (Activity) method.invoke(target);
            return activity;

        } else if (fragmentV4Class.isAssignableFrom(targetClass)) {

            Method method = targetClass.getMethod("getActivity");
            Activity activity = (Activity) method.invoke(target);
            return activity;

        } else if (fragmentXClass.isAssignableFrom(targetClass)) {

            Method method = targetClass.getMethod("getActivity");
            Activity activity = (Activity) method.invoke(target);
            return activity;

        }

        return null;
    }

    /**
     * 是否为可传输类型
     *
     * @param field the field
     * @return boolean boolean
     */
    private static boolean isBundleType(Field field) {
        Class<?> type = field.getType();

        if (type.isPrimitive()) {
            return true;
        } else if (
                String.class.isAssignableFrom(type)
                        || Integer.class.isAssignableFrom(type)
                        || Long.class.isAssignableFrom(type)
                        || Float.class.isAssignableFrom(type)
                        || Double.class.isAssignableFrom(type)
                        || Boolean.class.isAssignableFrom(type)
                        || Parcelable.class.isAssignableFrom(type)
                        || Serializable.class.isAssignableFrom(type)
        ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets requested fields.
     *
     * @param clazz the clazz
     * @return the declared fields
     */
    private static List<Field> getRequestedFields(@NonNull Class clazz) {
        List<Field> fieldList;
        for (fieldList = new ArrayList(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            int length = fields.length;

            for (int i = 0; i < length; ++i) {
                Field field = fields[i];
                Requested annotation = field.getAnnotation(Requested.class);
                if (annotation != null) {
                    fieldList.add(field);
                }
            }
        }

        return fieldList;
    }

    /**
     * New instance t.
     *
     * @param <T>   the type parameter
     * @param claxx the claxx
     * @return the t
     * @throws Exception the exception
     */
    private static <T> T newInstance(Class<T> claxx) throws Exception {
        Constructor<?>[] cons = claxx.getDeclaredConstructors();
        for (Constructor<?> c : cons) {
            Class[] cls = c.getParameterTypes();
            if (cls.length == 0) {
                c.setAccessible(true);
                return (T) c.newInstance();
            } else {
                Object[] objs = new Object[cls.length];
                for (int i = 0; i < cls.length; i++) {
                    objs[i] = getDefaultPrimitiveValue(cls[i]);
                }
                c.setAccessible(true);
                return (T) c.newInstance(objs);
            }
        }
        return null;
    }

    /**
     * Gets default primitive value.
     *
     * @param clazz the clazz
     * @return the default primitive value
     */
    private static Object getDefaultPrimitiveValue(Class clazz) {
        if (clazz.isPrimitive()) {
            return clazz == boolean.class ? false : 0;
        }
        return null;
    }

    /**
     * Context context.
     *
     * @return the context
     */
    public Context context() {
        try {
            Class<?> targetClass = Class.forName("com.rratchet.android.router.Router");
            Method method = targetClass.getDeclaredMethod("context");
            method.setAccessible(true);
            Context context = (Context) method.invoke(targetClass);
            return context;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Configs map.
     *
     * @return the map
     */
    public Map<RouteType, Map<String, Attribute>> configs() {
        try {
            Class<?> targetClass = Class.forName("com.rratchet.android.router.Router");
            Method method = targetClass.getDeclaredMethod("configs");
            method.setAccessible(true);
            Map<RouteType, Map<String, Attribute>> value = (Map<RouteType, Map<String, Attribute>>) method.invoke(targetClass);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化
     */
    public synchronized void init() {

        Context context = context();

        if (context == null) {
            return;
        }

        Set<String> classNames = null;

        String packageName = Constants.PACKAGE_NAME_TABLES;

        // 调试模式或者是新版本，会有新的类生成，因此需要重新加载，
        if (Router.debuggable() || Router.isAppInDebug(context) || PackageUtils.isNewVersion(context)) {
            try {
                // 扫面由RouterProcessor生成的路由启动加载器
                Set<String> set = ClassUtils.getFileNameByPackageName(context, packageName);
                classNames = filterVerify(set);
                CacheUtils.updateBootstrapper(context, classNames);
                PackageUtils.updateVersion(context);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            classNames = CacheUtils.getBootstrapper(context);
        }

        Set<IRouteBootstrapper> bootstrappers = new HashSet<>();

        if (classNames != null) {
            String prefix = Constants.PACKAGE_NAME_TABLES;
            String suffix = Constants.SUFFIX_OF_BOOTSTRAPPER;

            for (String className : classNames) {
                if (className.startsWith(prefix) && className.endsWith(suffix)) {
                    try {
                        Class<?> bootstrapperClass = Class.forName(className);

                        // 创建实例
                        Object bootstrapper = newInstance(bootstrapperClass);

                        if (bootstrapper instanceof IRouteBootstrapper) {
                            // 增加到集合中
                            bootstrappers.add((IRouteBootstrapper) bootstrapper);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        try {
            Map<RouteType, Map<String, Attribute>> configs = configs();
            if (configs != null) {
                for (IRouteBootstrapper bootstrapper : bootstrappers) {
                    bootstrapper.bootstrap(configs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inject.
     *
     * @param target the target
     * @throws Exception the exception
     */
    public void inject(@NonNull Object target) throws Exception {

        RouteBundle bundle = obtainBundle(target);
        inject(target, bundle);

    }

    /**
     * Inject.
     *
     * @param target the target
     * @param bundle the bundle
     * @throws Exception the exception
     */
    public void inject(@NonNull Object target, RouteBundle bundle) throws Exception {

        Context context = context();

        if (context == null) {
            return;
        }

        Map<RouteType, Map<String, Attribute>> configs = configs();

        if (configs == null || configs.isEmpty()) {
            return;
        }

        Context targetContext = obtainContext(target);
        if (targetContext == null) {
            targetContext = context;
        }

        Class clazz = target.getClass();
        List<Field> fields = getRequestedFields(clazz);
        if (fields == null || fields.isEmpty()) {
            return;
        }

        for (Field field : fields) {

            try {

                field.setAccessible(true);

                if (isBundleType(field)) {
                    injectBasic(target, bundle, field);
                } else {
                    // 默认注入对象
                    injectProvider(targetContext, configs, target, field);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inject basic.
     *
     * @param target the target
     * @param bundle the bundle
     * @param field  the field
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean injectBasic(Object target, RouteBundle bundle, Field field) throws Exception {


        Requested annotation = field.getAnnotation(Requested.class);
        String key = annotation.name();

        // 如果未定义需要注入的name则取字段名
        if (key == null || key.isEmpty()) {
            key = field.getName();
        }

        if (!bundle.containsKey(key)) {
            return false;
        }

        Class<?> type = field.getType();

        Object targetValue = field.get(target);

        if (int.class.isAssignableFrom(type)) {
            targetValue = bundle.getInt(key);
        } else if (double.class.isAssignableFrom(type)) {
            targetValue = bundle.getDouble(key);
        } else if (float.class.isAssignableFrom(type)) {
            targetValue = bundle.getFloat(key);
        } else if (long.class.isAssignableFrom(type)) {
            targetValue = bundle.getLong(key);
        } else if (boolean.class.isAssignableFrom(type)) {
            targetValue = bundle.getBoolean(key);
        } else {

            if (String.class.isAssignableFrom(type)) {
                targetValue = bundle.getString(key, targetValue != null ? (String) targetValue : "");
            } else if (Integer.class.isAssignableFrom(type)) {
                targetValue = bundle.getInt(key, targetValue != null ? (Integer) targetValue : 0);
            } else if (Long.class.isAssignableFrom(type)) {
                targetValue = bundle.getLong(key, targetValue != null ? (Long) targetValue : 0L);
            } else if (Float.class.isAssignableFrom(type)) {
                targetValue = bundle.getFloat(key, targetValue != null ? (Float) targetValue : 0.0F);
            } else if (Double.class.isAssignableFrom(type)) {
                targetValue = bundle.getDouble(key, targetValue != null ? (Double) targetValue : 0.0D);
            } else if (Boolean.class.isAssignableFrom(type)) {
                targetValue = bundle.getBoolean(key, targetValue != null ? (Boolean) targetValue : false);
            } else if (Parcelable.class.isAssignableFrom(type)) {
                targetValue = bundle.getParcelable(key);
            } else if (Serializable.class.isAssignableFrom(type)) {
                targetValue = bundle.getSerializable(key);
            } else {
                return false;
            }
        }

        field.set(target, targetValue);

        return true;
    }

    /**
     * Inject provider boolean.
     *
     * @param context the context
     * @param configs the configs
     * @param target  the target
     * @param field   the field
     * @return the boolean
     */
    public boolean injectProvider(Context context, Map<RouteType, Map<String, Attribute>> configs, Object target, Field field) {

        if (context == null) {
            return false;
        }

        if (configs == null || configs.isEmpty()) {
            return false;
        }

        Requested annotation = field.getAnnotation(Requested.class);
        String key = annotation.name();

        // 如果未定义需要注入的name则取字段名
        if (key == null || key.isEmpty()) {
            key = field.getName();
        }

        String providerKey = target.getClass().getName() + "#" + key;

        Class<?> providerClass = providersCache.get(providerKey);

        try {
            if (providerClass != null) {

                Object value = newInstance(providerClass);
                field.set(target, value);
            } else {
                // 从集合中获取，默认为Provider 类型
                Map<String, Attribute> table = configs.get(RouteType.PROVIDER);

                Attribute attribute = table.get(key);
                if (attribute != null && field.getType().isAssignableFrom(attribute.getTarget())) {
                    // 匹配 name 以及 类型
                    providerClass = attribute.getTarget();
                } else {

                    for (Map.Entry<String, Attribute> entry : table.entrySet()) {
                        // 默认只配对第一个相同类型的
                        Class<?> targetClass = entry.getValue().getTarget();
                        if (field.getType().isAssignableFrom(targetClass)) {
                            providerClass = targetClass;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (providerClass != null) {
            providersCache.put(providerKey, providerClass);

            try {
                // 实例化provider
                IProvider provider = (IProvider) newInstance(providerClass);
                // 执行init方法
                provider.init(context);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public void start(final Context context, final RouteInfo routeInfo) {


        if (routeInfo.getAttribute() == null) {
            RuntimeException exception = new RuntimeException("Not found attribute!");
            exception.printStackTrace();
            return;
        }

        /**
         * 暂时只支持Activity
         */
        switch (routeInfo.getAttribute().getRouteType()) {

            case ACTIVITY:

                runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(context, routeInfo);
                    }
                });
                break;

            default:

                break;
        }
    }

    public Object get(RouteInfo routeInfo) {

        Attribute attribute = routeInfo.getAttribute();

        if (attribute == null) {
            RuntimeException exception = new RuntimeException("Not found attribute!");
            exception.printStackTrace();
            return null;
        }

        Class<?> targetClass = attribute.getTarget();

        /**
         *
         * Service：返回对应的class；
         * Fragment：返回实例对象
         * Provider：返回实例对象
         */
        switch (attribute.getRouteType()) {
            case SERVICE:
                return targetClass;

            case FRAGMENT:
                try {
                    Object target = newInstance(targetClass);
                    try {
                        Method method = targetClass.getMethod("setArguments", Bundle.class);
                        method.invoke(target, routeInfo.getOptions());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return target;
                } catch (Exception e) {
                    RuntimeException exception = new RuntimeException("The [" + attribute.getPath() + "] cannot get a fragment!");
                    exception.printStackTrace();
                    e.printStackTrace();
                }
                break;
            case PROVIDER:
                try {
                    Object target = newInstance(targetClass);
                    return target;
                } catch (Exception e) {
                    RuntimeException exception = new RuntimeException("The [" + attribute.getPath() + "] cannot get a provider!");
                    exception.printStackTrace();
                    e.printStackTrace();
                }
                break;

            default:

                break;
        }

        return null;
    }

    /**
     * Be sure execute in main thread.
     *
     * @param runnable code
     */
    public void runInMainThread(Runnable runnable) {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            if (mHandler == null) {
                mHandler = new Handler(Looper.getMainLooper());
            }
            mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    private void startActivity(Context context, RouteInfo routeInfo) {

        Attribute attribute = routeInfo.getAttribute();

        Class<?> targetClass = attribute.getTarget();

        Intent intent = new Intent();
        Bundle options = routeInfo.getOptions();

        intent.setClass(context, targetClass);

        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        int flags = routeInfo.getFlags();
        if (flags != -1) {
            intent.addFlags(flags);
        }

        String action = routeInfo.getAction();
        if (action != null && !action.isEmpty()) {
            intent.setAction(action);
        }

        String url = attribute.getPath();

        Map<String, String> params = routeInfo.getParams();
        if (params != null && !params.isEmpty()) {

            url += buildParams(params);

            if (options == null || options.isEmpty()) {
                options = new Bundle();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    options.putString(entry.getKey(), entry.getValue());
                }
            }
        }

        intent.setData(Uri.parse(url));

        int requestCode = routeInfo.getRequestCode();
        if (requestCode > 0) {
            if (context instanceof Activity) {
                ActivityCompat.startActivityForResult((Activity) context, intent, requestCode, options);
            } else {
                Log.w(Router.TAG, "Must use [start(activity)] to support [startActivityForResult]");
                // 使用正常跳转
                ActivityCompat.startActivity(context, intent, options);
            }
        } else {
            ActivityCompat.startActivity(context, intent, options);
        }

        if (routeInfo.getEnterAnim() != -1 && routeInfo.getExitAnim() != -1 && context instanceof Activity) {
            ((Activity) context).overridePendingTransition(routeInfo.getEnterAnim(), routeInfo.getExitAnim());
        }
    }

    private String buildParams(Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        Set<String> keys = params.keySet();
        int i = 0;
        Iterator var4 = keys.iterator();

        while (var4.hasNext()) {
            String key = (String) var4.next();
            String value = (String) params.get(key);
            if (value != null) {
                if (i == 0) {
                    builder.append('?');
                }

                try {
                    builder.append(key).append('=').append(URLEncoder.encode(value, "UTF-8"));
                } catch (Exception var8) {
                    var8.printStackTrace();
                }

                if (i < keys.size() - 1) {
                    builder.append('&');
                }

                ++i;
            }
        }

        return builder.toString();
    }
}
