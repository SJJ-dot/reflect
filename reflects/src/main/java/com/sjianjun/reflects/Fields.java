package com.sjianjun.reflects;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Fields {

    public static boolean setStaticValue(String clazzName, String fieldName, Object value) {
        Field field = getField(clazzName, fieldName);
        if (field != null) {
            return setStaticValue(field, value);
        }
        return false;
    }

    public static boolean setStaticValue(Class<?> clazz, String fieldName, Object value) {
        Field field = getField(clazz, fieldName);
        if (field != null) {
            return setStaticValue(field, value);
        }
        return false;
    }

    public static boolean setStaticValue(Field field, Object value) {
        return setValue(null, field, value);
    }

    @Nullable
    public static Object getStaticValue(String clazzName, String fieldName) {
        try {
            Class<?> forName = Class.forName(clazzName);
            return getValue(forName, getField(forName, fieldName));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Object getStaticValue(Class<?> clazz, String fieldName) {
        return getValue(clazz, getField(clazz, fieldName));
    }

    @Nullable
    public static Object getStaticValue(Class<?> clazz, Field field) {
        return getValue(clazz, field);
    }

    public static boolean setValue(Object target, String fieldName, Object value) {
        Field field = getField(target.getClass(), fieldName);
        return setValue(target, field, value);
    }

    public static boolean setValue(Object target, Field field, Object value) {
        try {
            field.set(target, value);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    @Nullable
    public static Object getValue(Object target, String fieldName) {
        return getValue(target, getField(target.getClass(), fieldName));
    }

    @Nullable
    public static Object getValue(Object target, Field field) {
        try {
            return field.get(target);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Field getField(String className, String fieldName) {
        try {
            return getField(Class.forName(className), fieldName);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Field getField(Class<?> clazz, String fieldName) {
        Field cacheField = getCacheField(clazz, fieldName);
        if (cacheField != null) {
            return cacheField;
        }
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            try {
                Field field = c.getDeclaredField(fieldName);
                //设置忽略Java的访问权限检查。
                field.setAccessible(true);
                putCacheField(clazz, field);
                return field;
            } catch (NoSuchFieldException e) {
                //nothing to do
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }
        for (Class<?> clazzInterface : clazz.getInterfaces()) {
            Field field = getInterfaceField(clazzInterface, fieldName);
            if (field != null) {
                putCacheField(clazz, field);
                return field;
            }
        }
        return null;
    }

    @Nullable
    private static Field getInterfaceField(Class<?> clazzInterface, String fieldName) {
        if (clazzInterface == null) {
            return null;
        }
        try {
            Field field = clazzInterface.getDeclaredField(fieldName);
            //设置忽略Java的访问权限检查。
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            //nothing to do
            for (Class<?> clazz : clazzInterface.getInterfaces()) {
                Field field = getInterfaceField(clazz, fieldName);
                if (field != null) {
                    return field;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    private static final Map<String, Field> classFieldMap = new ConcurrentHashMap<>();

    private static String getKey(Class<?> clazz, String fieldName) {
        return clazz.getName() + "#" + fieldName;
    }

    private static Field getCacheField(Class<?> clazz, String fieldName) {
        return classFieldMap.get(getKey(clazz, fieldName));
    }

    private static void putCacheField(Class<?> clazz, Field field) {
        classFieldMap.put(getKey(clazz, field.getName()), field);
    }

    public void cleanFieldCache() {
        classFieldMap.clear();
    }
}
