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
 * 文件名称：TypeUtils.java
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

package com.rratchet.android.router.compiler.utils;

import com.rratchet.android.router.support.enums.FieldType;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.rratchet.android.router.compiler.utils.Constants.AndroidType.PARCELABLE;
import static com.rratchet.android.router.compiler.utils.Constants.JavaType.BOOLEAN;
import static com.rratchet.android.router.compiler.utils.Constants.JavaType.BYTE;
import static com.rratchet.android.router.compiler.utils.Constants.JavaType.CHAR;
import static com.rratchet.android.router.compiler.utils.Constants.JavaType.DOUBLE;
import static com.rratchet.android.router.compiler.utils.Constants.JavaType.FLOAT;
import static com.rratchet.android.router.compiler.utils.Constants.JavaType.INTEGER;
import static com.rratchet.android.router.compiler.utils.Constants.JavaType.LONG;
import static com.rratchet.android.router.compiler.utils.Constants.JavaType.SERIALIZABLE;
import static com.rratchet.android.router.compiler.utils.Constants.JavaType.SHORT;
import static com.rratchet.android.router.compiler.utils.Constants.JavaType.STRING;

/**
 * <pre>
 *
 *      作 者 :        ASLai(laijianhua@ruixiude.com).
 *      日 期 :        2019-07-17
 *      版 本 :        V1.0
 *      描 述 :        description
 *
 *
 * </pre>
 *
 * @author ASLai
 */
public class TypeUtils {

    private Types      types;
    private TypeMirror parcelableType;
    private TypeMirror serializableType;

    public TypeUtils(Types types, Elements elements) {
        this.types = types;

        parcelableType = elements.getTypeElement(PARCELABLE).asType();
        serializableType = elements.getTypeElement(SERIALIZABLE).asType();
    }

    /**
     * Diagnostics out the true java type
     *
     * @param element Raw type
     * @return Type class of java
     */
    public FieldType typeExchange(Element element) {
        TypeMirror typeMirror = element.asType();


        switch (typeMirror.toString()) {
            case BYTE:
                return FieldType.BYTE;
            case SHORT:
                return FieldType.SHORT;
            case INTEGER:
                return FieldType.INT;
            case LONG:
                return FieldType.LONG;
            case FLOAT:
                return FieldType.FLOAT;
            case DOUBLE:
                return FieldType.DOUBLE;
            case BOOLEAN:
                return FieldType.BOOLEAN;
            case CHAR:
                return FieldType.CHAR;
            case STRING:
                return FieldType.STRING;
            default:
                // Other side, maybe the PARCELABLE or SERIALIZABLE or OBJECT.
                if (types.isSubtype(typeMirror, parcelableType)) {
                    // PARCELABLE
                    return FieldType.PARCELABLE;
                } else if (types.isSubtype(typeMirror, serializableType)) {
                    // SERIALIZABLE
                    return FieldType.SERIALIZABLE;
                } else {
                    return FieldType.OBJECT;
                }
        }
    }
}
