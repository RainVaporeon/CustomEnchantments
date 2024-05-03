package io.github.rainvaporeon.customenchantments.util;

import java.util.Objects;
import java.util.Set;

public class SetCollection {
    public static <T> T find(Set<T> set, T object) {
        return set.stream().filter(t -> Objects.equals(t, object)).findFirst().orElse(null);
    }

    /**
     * Forcibly adds an element to the set, removing the originally mapped key if needed
     * @param set the set
     * @param object the object
     * @return whether there was already a mapping
     * @param <T> the type
     */
    public static <T> boolean addForced(Set<T> set, T object) {
        return set.remove(object) && set.add(object);
    }
}
