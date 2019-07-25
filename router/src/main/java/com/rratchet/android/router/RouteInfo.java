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
 * 文件名称：RouteInfo.java
 * 文件描述：
 *
 * 创 建 人：ASLai(laijianhua@ruixiude.com)
 *
 * 上次修改时间：2019-07-25 12:16:01
 *
 * 修 改 人：ASLai(laijianhua@ruixiude.com)
 * 修改时间：2019-07-25 14:08:24
 * 修改备注：
 */

package com.rratchet.android.router;

import android.content.Context;
import android.os.Bundle;

import com.rratchet.android.router.support.model.Attribute;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * <pre>
 *
 *      作 者 :        ASLai(laijianhua@ruixiude.com).
 *      日 期 :        2019-07-24
 *      版 本 :        V1.0
 *      描 述 :        description
 *
 *
 * </pre>
 *
 * @author ASLai
 */
public class RouteInfo {

    /**
     * 目标路由属性
     */
    private Attribute           attribute;
    /**
     * {@link android.content.Intent#setAction(String)}
     */
    private String              action;
    /**
     * The Request code.
     */
    private int                 requestCode = -1;
    /**
     * 原始数据，{@link android.content.Intent#putExtra(String, Object)}
     */
    private Bundle              options;
    /**
     * The url params.
     */
    private Map<String, String> params;
    /**
     * The Flags.
     */
    private int                 flags       = -1;
    /**
     * The Enter anim.
     */
    private int                 enterAnim   = -1;
    /**
     * The Exit anim.
     */
    private int                 exitAnim    = -1;

    /**
     * Gets action.
     *
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets action.
     *
     * @param action the action
     * @return the action
     */
    public RouteInfo setAction(String action) {
        this.action = action;
        return this;
    }

    /**
     * Gets attribute.
     *
     * @return the attribute
     */
    public Attribute getAttribute() {
        return attribute;
    }

    /**
     * Sets attribute.
     *
     * @param attribute the attribute
     * @return the attribute
     */
    public RouteInfo setAttribute(Attribute attribute) {
        this.attribute = attribute;
        return this;
    }

    /**
     * Gets request code.
     *
     * @return the request code
     */
    public int getRequestCode() {
        return requestCode;
    }

    /**
     * Sets request code.
     *
     * @param requestCode the request code
     * @return the request code
     */
    public RouteInfo setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    /**
     * Gets options.
     *
     * @return the options
     */
    public Bundle getOptions() {
        return options;
    }

    /**
     * Sets options.
     *
     * @param options the options
     * @return the options
     */
    public RouteInfo setOptions(Bundle options) {
        this.options = options;
        return this;
    }

    /**
     * Gets params.
     *
     * @return the params
     */
    public Map<String, String> getParams() {
        return params;
    }

    /**
     * Sets params.
     *
     * @param params the params
     * @return the params
     */
    public RouteInfo setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    /**
     * Add param route info.
     *
     * @param key   the key
     * @param value the value
     * @return the route info
     */
    public RouteInfo addParam(String key, Object value) {
        if (params == null) {
            params = new IdentityHashMap<>();
        }
        if (value == null) {
            params.put(key, "");
        } else {
            params.put(key, String.valueOf(value));
        }
        return this;
    }

    /**
     * Gets flags.
     *
     * @return the flags
     */
    public int getFlags() {
        return flags;
    }

    /**
     * 添加附加标记
     *
     * @param flags the flags
     * @see android.content.Intent#addFlags(int) android.content.Intent#addFlags(int)
     */
    public void addFlags(int flags) {
        this.flags |= flags;
    }

    /**
     * Gets enter anim.
     *
     * @return the enter anim
     */
    public int getEnterAnim() {
        return enterAnim;
    }

    /**
     * Gets exit anim.
     *
     * @return the exit anim
     */
    public int getExitAnim() {
        return exitAnim;
    }

    /**
     * 设置过渡效果
     *
     * @param enterAnim the enter anim
     * @param exitAnim  the exit anim
     * @return the pending transition
     * @see android.app.Activity#overridePendingTransition(int, int) android.app.Activity#overridePendingTransition(int, int)
     */
    public RouteInfo setPendingTransition(int enterAnim, int exitAnim) {
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
        return this;
    }

    /**
     * Start.
     *
     * @param context the context
     */
    public void start(Context context) {
        Router.start(context, this);
    }

    /**
     * Get t.
     *
     * @param <T> the type parameter
     * @return the t
     */
    public <T> T get() {
        Object target = Router.get(this);
        try {
            if (target != null) {
                return (T) target;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
