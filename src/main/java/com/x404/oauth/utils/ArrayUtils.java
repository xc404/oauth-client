package com.x404.oauth.utils;

import java.lang.reflect.Array;

/**
 * @Author chaox
 * @Date 12/24/2023 10:07 AM
 */
public final class ArrayUtils
{
    public static Object[] toObjectArray(Object source) {
        if( source instanceof Object[] ) {
            return (Object[]) source;
        }
        if( source == null ) {
            return new Object[0];
        }
        if( !source.getClass().isArray() ) {
            throw new IllegalArgumentException("Source is not an array: " + source);
        }
        int length = Array.getLength(source);
        if( length == 0 ) {
            return new Object[0];
        }
        Class wrapperType = Array.get(source, 0).getClass();
        Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
        for( int i = 0; i < length; i++ ) {
            newArray[i] = Array.get(source, i);
        }
        return newArray;
    }
}
