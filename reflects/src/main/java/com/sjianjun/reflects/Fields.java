package com.sjianjun.reflects;

import java.lang.reflect.Field;

public final class Fields {
    public static final Field getField(Class<?> clazz,String fieldName,boolean forceAccess) {
        try {
            Field field = clazz.getDeclaredField("fieldName");

            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
