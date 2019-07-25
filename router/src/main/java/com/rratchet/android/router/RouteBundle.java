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
 * 文件名称：RouteBundle.java
 * 文件描述：
 *
 * 创 建 人：ASLai(laijianhua@ruixiude.com)
 *
 * 上次修改时间：2019-07-23 16:26:48
 *
 * 修 改 人：ASLai(laijianhua@ruixiude.com)
 * 修改时间：2019-07-25 14:08:24
 * 修改备注：
 */

package com.rratchet.android.router;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

/**
 * <pre>
 *
 *      作 者 :        ASLai(laijianhua@ruixiude.com).
 *      日 期 :        2019-07-23
 *      版 本 :        V1.0
 *      描 述 :        description
 *
 *
 * </pre>
 *
 * @author ASLai
 */
public class RouteBundle {

    private Bundle bundle;
    private Uri uri;

    public RouteBundle(Bundle bundle, Uri uri) {
        this.bundle = bundle;
        this.uri = uri;
        if (this.bundle == null) {
            this.bundle = new Bundle();
        }

    }

    public Bundle getBundle() {
        return this.bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public Uri getUri() {
        return this.uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public int getInt(String key) {
        return this.getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return this.getBoolean(key, false);
    }

    public long getLong(String key) {
        return this.getLong(key, 0L);
    }

    public double getDouble(String key) {
        return this.getDouble(key, 0.0D);
    }

    public float getFloat(String key) {
        return this.getFloat(key, 0.0F);
    }

    public String getString(String key) {
        return this.getString(key, (String)null);
    }

    public String getString(String key, String defaultValue) {
        Object o = this.bundle.get(key);
        if (o == null && this.uri != null) {
            o = this.uri.getQueryParameter(key);
        }

        return o == null ? defaultValue : o.toString();
    }

    public double getDouble(String key, double defaultValue) {
        Object o = this.bundle.get(key);
        if (o == null && this.uri != null) {
            o = this.uri.getQueryParameter(key);
        }

        if (o == null) {
            return defaultValue;
        } else {
            try {
                return o instanceof String ? Double.parseDouble(o.toString()) : (Double)o;
            } catch (ClassCastException var6) {
                typeWarning(key, o, "Double", defaultValue, var6);
                return defaultValue;
            }
        }
    }

    public long getLong(String key, long defaultValue) {
        Object o = this.bundle.get(key);
        if (o == null && this.uri != null) {
            o = this.uri.getQueryParameter(key);
        }

        if (o == null) {
            return defaultValue;
        } else {
            try {
                return o instanceof String ? Long.parseLong(o.toString()) : (Long)o;
            } catch (ClassCastException var6) {
                typeWarning(key, o, "Long", defaultValue, var6);
                return defaultValue;
            }
        }
    }

    public int getInt(String key, int defaultValue) {
        Object o = this.bundle.get(key);
        if (o == null && this.uri != null) {
            o = this.uri.getQueryParameter(key);
        }

        if (o == null) {
            return defaultValue;
        } else {
            try {
                return o instanceof String ? Integer.parseInt(o.toString()) : (Integer)o;
            } catch (ClassCastException var5) {
                typeWarning(key, o, "Integer", defaultValue, var5);
                return defaultValue;
            }
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object o = this.bundle.get(key);
        if (o == null && this.uri != null) {
            o = this.uri.getQueryParameter(key);
        }

        if (o == null) {
            return defaultValue;
        } else {
            try {
                return o instanceof String ? Boolean.parseBoolean(o.toString()) : (Boolean)o;
            } catch (ClassCastException var5) {
                typeWarning(key, o, "Boolean", defaultValue, var5);
                return defaultValue;
            }
        }
    }

    public float getFloat(String key, float defaultValue) {
        Object o = this.bundle.get(key);
        if (o == null && this.uri != null) {
            o = this.uri.getQueryParameter(key);
        }

        if (o == null) {
            return defaultValue;
        } else {
            try {
                return o instanceof String ? Float.parseFloat(o.toString()) : (Float)o;
            } catch (ClassCastException var5) {
                typeWarning(key, o, "Float", defaultValue, var5);
                return defaultValue;
            }
        }
    }

    static void typeWarning(String key, Object value, String className, Object defaultValue, ClassCastException e) {
        StringBuilder sb = new StringBuilder();
        sb.append("Key ");
        sb.append(key);
        sb.append(" expected ");
        sb.append(className);
        sb.append(" but value was a ");
        sb.append(value.getClass().getName());
        sb.append(".  The default value ");
        sb.append(defaultValue);
        sb.append(" was returned.");
        Log.w(Router.TAG, sb.toString());
        Log.w(Router.TAG, "Attempt to cast generated internal exception:", e);
    }

    public Object get(String key) {
        return this.bundle.get(key);
    }

    public boolean containsKey(String key) {
        return this.bundle.containsKey(key) || this.uri != null && this.uri.getQueryParameter(key) != null;
    }

    public <T extends Parcelable> T getParcelable(String key) {
        return this.bundle.getParcelable(key);
    }

    public Serializable getSerializable(String key) {
        return this.bundle.getSerializable(key);
    }
}
