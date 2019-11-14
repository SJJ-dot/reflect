package com.sjianjun.reflects;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Methods {
    @Nullable
    public static Object newInstance(String clazz, Object[] args) {
        try {
            return newInstance(Class.forName(clazz), args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static <T> T newInstance(Class<T> clazz, Object[] args) {
        if (clazz == null) {
            return null;
        }
        try {
            Constructor<T> constructor = clazz.getConstructor(toClasss(args));
            return constructor.newInstance(args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Object invokeStatic(String clazz, String methodName, Object[] args) {
        try {
            Method method = getMethod(clazz, methodName, toClasss(check(args)));
            invokeStatic(method, args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Object invokeStatic(Class<?> clazz, String methodName, Object[] args) {
        try {
            Method method = getMethod(clazz, methodName, toClasss(check(args)));
            invokeStatic(method, args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Object invokeStatic(Method method, Object[] args) {
        try {
            method.invoke(null, check(args));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Object invoke(Object obj, String methodName, Object[] args) {
        if (obj == null) {
            return null;
        }
        Method method = getMethod(obj.getClass(), methodName, toClasss(check(args)));
        return invoke(obj, method, args);
    }

    @Nullable
    public static Object invoke(Object obj, Method method, Object[] args) {
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(obj, check(args));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Method getMethod(String clazzName, String methodName, Class<?>[] parameterTypes) {
        try {
            return getMethod(Class.forName(clazzName), methodName, parameterTypes);
        } catch (ClassNotFoundException ignored) {
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        if (clazz == null) {
            return null;
        }
        if (methodName == null || methodName.length() == 0) {
            return null;
        }
        parameterTypes = check(parameterTypes);
        for (Class<?> c = clazz; c != null; c = clazz.getSuperclass()) {
            try {
                Method method = c.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (Throwable ignored) {
            }
        }
        for (Class<?> clazzInterface : clazz.getInterfaces()) {
            Method method = getInterfaceMethod(clazzInterface, methodName, parameterTypes);
            if (method != null) {
                putCacheMethod(clazz, method);
                return method;
            }
        }
        return null;
    }

    @Nullable
    private static Method getInterfaceMethod(Class<?> clazzInterface, String methodName, Class<?>[] parameterTypes) {
        if (clazzInterface == null) {
            return null;
        }
        try {
            return clazzInterface.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            //nothing to do
            for (Class<?> clazz : clazzInterface.getInterfaces()) {
                Method method = getInterfaceMethod(clazz, methodName, parameterTypes);
                if (method != null) {
                    return method;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    private static final Class<?>[] EMPTY_CLASS = new Class[0];
    private static final Object[] EMPTY_OBJ = new Object[0];

    private static Class<?>[] check(Class<?>[] parameterTypes) {
        if (parameterTypes == null) {
            return EMPTY_CLASS;
        }
        return parameterTypes;
    }

    private static Object[] check(Object[] args) {
        if (args == null) {
            return EMPTY_OBJ;
        }
        return args;
    }

    private static Class<?>[] toClasss(Object[] objects) {
        if (objects == null) {
            return EMPTY_CLASS;
        }
        Class<?>[] classes = new Class[objects.length];
        for (int i = 0; i < objects.length; i++) {
            classes[i] = objects[i].getClass();
        }
        return classes;
    }

    private static final Map<String, Method> classMethodMap = new ConcurrentHashMap<>();

    private static String getKey(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        StringBuilder builder = new StringBuilder(clazz.getName());
        builder.append("#");
        builder.append(methodName);
        for (Class<?> type : parameterTypes) {
            builder.append("#").append(type.getName());
        }
        return builder.toString();
    }

    private static Method getCacheMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        return classMethodMap.get(getKey(clazz, methodName, parameterTypes));
    }

    private static void putCacheMethod(Class<?> clazz, Method method) {
        classMethodMap.put(getKey(clazz, method.getName(), method.getParameterTypes()), method);
    }

    public void cleanMethodCache() {
        classMethodMap.clear();
    }
}
