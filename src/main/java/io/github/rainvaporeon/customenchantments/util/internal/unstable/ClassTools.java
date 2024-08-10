package io.github.rainvaporeon.customenchantments.util.internal.unstable;

import com.google.common.annotations.Beta;
import io.github.rainvaporeon.customenchantments.util.internal.Open;
import io.github.rainvaporeon.customenchantments.util.internal.exceptions.InvocationException;
import org.jetbrains.annotations.ApiStatus;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;

public final class ClassTools {
    /**
     * Instantiates an enum class
     * @param type the enum type
     * @param argt the constructor argument type
     * @param args the constructor parameters; must be the same length as argt
     * @return the enum
     * @param <T> the enum type
     * @throws InvocationException if an exception occurs whilst executing
     */
    @SuppressWarnings("unchecked") @ApiStatus.Internal @Beta
    public static <T extends Enum<T>> T instantiateClass(Class<T> type, Class<?>[] argt, Object... args) throws InvocationException {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor(argt);
            constructor.setAccessible(true);
            MethodHandle mh = MethodHandles.lookup().unreflectConstructor(constructor);
            return (T) mh.invokeWithArguments(args);
        } catch (Throwable t) {
            throw new InvocationException(t);
        }
    }

    @Open
    public static Class<?>[] mapToType(Object... arr) {
        Class<?>[] c = new Class[arr.length];
        for (int i = 0; i < c.length; i++) c[i] = arr[i].getClass();
        return c;
    }
}
