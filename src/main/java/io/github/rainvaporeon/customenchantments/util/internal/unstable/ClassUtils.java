package io.github.rainvaporeon.customenchantments.util.internal.unstable;

import io.github.rainvaporeon.customenchantments.util.internal.Open;

@Open
public final class ClassUtils {
    public static Class<?>[] mapToType(Object... arr) {
        Class<?>[] c = new Class[arr.length];
        for (int i = 0; i < c.length; i++) c[i] = arr[i].getClass();
        return c;
    }
}
