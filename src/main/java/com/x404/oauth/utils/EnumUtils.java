/**
 * Copyright (c) 2008, http://www.snakeyaml.org
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.x404.oauth.utils;

public class EnumUtils
{

    /**
     * Looks for an enumeration constant that matches the string without being case sensitive
     *
     * @param enumType - the Class object of the enum type from which to return a constant
     * @param name     - the name of the constant to return
     * @param <T>      - the enum type whose constant is to be returned
     * @return the enum constant of the specified enum type with the specified name, insensitive to case
     */
    public static <T extends Enum<T>> T findEnumInsensitiveCase(Class<T> enumType, String name) {
        for( T constant : enumType.getEnumConstants() ) {
            if( constant.name().compareToIgnoreCase(name) == 0 ) {
                return constant;
            }
        }
        return null;
    }
}
