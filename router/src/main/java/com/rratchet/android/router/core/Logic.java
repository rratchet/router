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
 * 文件名称：Logic.java
 * 文件描述：
 *
 * 创 建 人：ASLai(laijianhua@ruixiude.com)
 *
 * 上次修改时间：2019-07-24 11:12:40
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

import androidx.annotation.NonNull;

import com.rratchet.android.router.RouteBundle;
import com.rratchet.android.router.Router;
import com.rratchet.android.router.support.annotation.Requested;
import com.rratchet.android.router.template.IRouteBootstrapper;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <pre>
 *
 *      作 者 :        ASLai(laijianhua@ruixiude.com).
 *      日 期 :        2019-07-22
 *      版 本 :        V1.0
 *      描 述 :        description
 *
 *
 * </pre>
 *
 * @author ASLai
 */
public class Logic {

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
     * 初始化服务管理类，在使用该类时确保先进行初始化
     *
     * @param context  the context
     */
    public static synchronized void init(Context context) {

        Set<String> classNames = null;

        String packageName = Constants.PACKAGE_NAME_TABLES;

        // 调试模式或者是新版本，会有新的类生成，因此需要重新加载，
        if (Router.debuggable() || Router.isAppInDebug(context) || PackageUtils.isNewVersion(context)) {
            try {
                // 扫面由SupportProcessor生成的组件加载器
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

                        Object bootstrapper = bootstrapperClass.getConstructor().newInstance();

                        if (bootstrapper instanceof IRouteBootstrapper) {
                            bootstrappers.add((IRouteBootstrapper) bootstrapper);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        try {

            if (!bootstrappers.isEmpty()) {

//                Router router = newInstance(Router.class);
//                Map<RouteType, Map<String, Attribute>> configs = router.configs();
//
//                for (IRouteBootstrapper bootstrapper : bootstrappers) {
//                    bootstrapper.bootstrap(configs);
//                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets requested fields.
     *
     * @param clazz the clazz
     * @return the declared fields
     */
    static List<Field> getRequestedFields(@NonNull Class clazz) {
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
     * Inject.
     *
     * @param context the context
     * @param target  the target
     * @throws Exception the exception
     */
    public static void inject(@NonNull Context context, @NonNull Object target) throws Exception {

        RouteBundle bundle = obtainBundle(target);
        inject(context, target, bundle);

    }

    /**
     * Inject.
     *
     * @param context the context
     * @param target  the target
     * @param bundle  the bundle
     * @throws Exception the exception
     */
    public static void inject(@NonNull Context context, @NonNull Object target, RouteBundle bundle) throws Exception {


        Class clazz = target.getClass();
        List<Field> fields = getRequestedFields(clazz);
        if (fields == null || fields.isEmpty()) {
            return;
        }

        String className = target.getClass().getName();

        for (Field field : fields) {

            try {

                field.setAccessible(true);

                String typeName = field.getGenericType().toString();

                Class<?> type = field.getType();

                if (injectBasic(target, bundle, field)) {

                } else {
                    // 默认注入对象

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
     * Inject basic.
     *
     * @param target the target
     * @param bundle the bundle
     * @param field  the field
     * @throws Exception the exception
     */
    private static boolean injectBasic(Object target, RouteBundle bundle, Field field) throws Exception {


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
                targetValue = bundle.getInt(key,targetValue != null ? (Integer) targetValue : 0);
            } else if (Long.class.isAssignableFrom(type)) {
                targetValue = bundle.getLong(key, targetValue != null ? (Long) targetValue : 0L);
            } else if (Float.class.isAssignableFrom(type)) {
                targetValue = bundle.getFloat(key, targetValue != null ? (Float) targetValue : 0.0F);
            } else if (Double.class.isAssignableFrom(type)) {
                targetValue = bundle.getDouble(key, targetValue != null ? (Double) targetValue : 0.0D);
            } else if (Boolean.class.isAssignableFrom(type)) {
                targetValue = bundle.getBoolean(key,targetValue != null ? (Boolean) targetValue : false);
            } else {
                return false;
            }
        }

        field.set(target, targetValue);

        return true;
    }

    private static boolean injectProvider(Context context, Object target, Field field) {

        return false;
    }

    public static <T> T newInstance(Class<T> claxx) throws Exception {
        Constructor<?>[] cons = claxx.getDeclaredConstructors();
        for (Constructor<?> c : cons) {
            Class[] cls = c.getParameterTypes();
            if (cls.length == 0) {
                c.setAccessible(true);
                return (T) c.newInstance();
            } else {
                Object[] objs = new Object[cls.length];
                for (int i = 0; i < cls.length; i++) {
                    objs[i] = getDefaultPrimiticeValue(cls[i]);
                }
                c.setAccessible(true);
                return (T) c.newInstance(objs);
            }
        }
        return null;
    }

    public static Object getDefaultPrimiticeValue(Class clazz) {
        if (clazz.isPrimitive()) {
            return clazz == boolean.class ? false : 0;
        }
        return null;
    }
}
