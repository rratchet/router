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
 * 文件名称：Attribute.java
 * 文件描述：
 *
 * 创 建 人：ASLai(laijianhua@ruixiude.com)
 *
 * 上次修改时间：2019-07-23 15:40:31
 *
 * 修 改 人：ASLai(laijianhua@ruixiude.com)
 * 修改时间：2019-07-25 14:08:24
 * 修改备注：
 */

package com.rratchet.android.router.support.model;

import com.rratchet.android.router.support.annotation.Requested;
import com.rratchet.android.router.support.enums.FieldType;
import com.rratchet.android.router.support.enums.RouteType;

import java.util.Map;

import javax.lang.model.element.Element;

/**
 * <pre>
 *
 *      作 者 :        ASLai(laijianhua@ruixiude.com).
 *      日 期 :        2019-07-18
 *      版 本 :        V1.0
 *      描 述 :        description
 *
 *
 * </pre>
 *
 * @author ASLai
 */
public class Attribute {

    /**
     * 路由类型
     */
    private RouteType routeType;

    /**
     * 原始类型
     */
    private Element rawType;

    /**
     * 目标类
     */
    private Class<?> target;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 别名
     */
    private String alias;

    /**
     * 备注
     */
    private String remark;

    /**
     * 数字越小，优先级越高
     */
    private int priority = 1000;

    /**
     * 注入的字段
     */
    private Map<String, Requested> injectFields;

    /**
     * 注入字段的类型
     */
    private Map<String, FieldType> fieldTypes;

    private Attribute(Builder builder) {
        setRouteType(builder.routeType);
        setRawType(builder.rawType);
        setTarget(builder.target);
        setPath(builder.path);
        setAlias(builder.alias);
        setRemark(builder.remark);
        setPriority(builder.priority);
        setInjectFields(builder.injectFields);
        setFieldTypes(builder.fieldTypes);
    }

    public static Attribute build(RouteType routeType, Class<?> target, String path, String alias, String remark, int priority) {
        return new Builder()
                .withRouteType(routeType)
                .withTarget(target)
                .withPath(path)
                .withAlias(alias)
                .withRemark(remark)
                .withPriority(priority)
                .build();
    }

    public static Attribute build(RouteType routeType, Class<?> target, String path, String alias, String remark, int priority, Map<String, Requested> injectFields, Map<String, FieldType> fieldTypes) {
        return new Builder()
                .withRouteType(routeType)
                .withTarget(target)
                .withPath(path)
                .withAlias(alias)
                .withRemark(remark)
                .withPriority(priority)
                .withInjectFields(injectFields)
                .withFieldTypes(fieldTypes)
                .build();
    }

    public RouteType getRouteType() {
        return routeType;
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    public Element getRawType() {
        return rawType;
    }

    public void setRawType(Element rawType) {
        this.rawType = rawType;
    }

    public Class<?> getTarget() {
        return target;
    }

    public void setTarget(Class<?> target) {
        this.target = target;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Map<String, Requested> getInjectFields() {
        return injectFields;
    }

    public void setInjectFields(Map<String, Requested> injectFields) {
        this.injectFields = injectFields;
    }

    public Map<String, FieldType> getFieldTypes() {
        return fieldTypes;
    }

    public void setFieldTypes(Map<String, FieldType> fieldTypes) {
        this.fieldTypes = fieldTypes;
    }


    public static final class Builder {
        private RouteType              routeType;
        private Element                rawType;
        private Class<?>               target;
        private String                 path;
        private String                 alias;
        private String                 remark;
        private int                    priority;
        private Map<String, Requested> injectFields;
        private Map<String, FieldType> fieldTypes;

        public Builder() {
        }

        public Builder withRouteType(RouteType routeType) {
            this.routeType = routeType;
            return this;
        }

        public Builder withRawType(Element rawType) {
            this.rawType = rawType;
            return this;
        }

        public Builder withTarget(Class<?> target) {
            this.target = target;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder withAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public Builder withRemark(String remark) {
            this.remark = remark;
            return this;
        }

        public Builder withPriority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder withInjectFields(Map<String, Requested> injectFields) {
            this.injectFields = injectFields;
            return this;
        }

        public Builder withFieldTypes(Map<String, FieldType> fieldTypes) {
            this.fieldTypes = fieldTypes;
            return this;
        }

        public Attribute build() {
            return new Attribute(this);
        }
    }
}
