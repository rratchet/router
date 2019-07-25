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
 * 模块名称：router-compiler
 *
 * 文件名称：Document.java
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

package com.rratchet.android.router.compiler.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.rratchet.android.router.compiler.utils.Check;

import java.util.List;

/**
 * <pre>
 *
 *      作 者 :        ASLai(laijianhua@ruixiude.com).
 *      日 期 :        2019-07-18
 *      版 本 :        V1.0
 *      描 述 :        路由配置文档
 *
 *
 * </pre>
 *
 * @author ASLai
 */
public class Document {

    @JSONField(ordinal = 1)
    private String      path;
    @JSONField(ordinal = 2)
    private String      alias;
    @JSONField(ordinal = 3)
    private String      description;
    @JSONField(ordinal = 4)
    private String      prototype;
    @JSONField(ordinal = 5)
    private String      className;
    @JSONField(ordinal = 6)
    private String      type;
    @JSONField(ordinal = 7)
    private int         mark;
    @JSONField(ordinal = 8)
    private List<Param> params;


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

    public String getPrototype() {
        return prototype;
    }

    public void setPrototype(String prototype) {
        this.prototype = prototype;
    }

    public void addPrototype(String prototype) {
        if (!Check.isEmpty(getPrototype())) {
            setPrototype(prototype);
        } else {
            setPrototype(getPrototype() + ", " + prototype);
        }
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (!Check.isEmpty(description)) {
            this.description = description;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public static class Param {
        @JSONField(ordinal = 1)
        private String  key;
        @JSONField(ordinal = 2)
        private String  type;
        @JSONField(ordinal = 3)
        private String  description;
        @JSONField(ordinal = 4)
        private boolean required;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            if (!Check.isEmpty(description)) {
                this.description = description;
            }
        }

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }
    }
}
