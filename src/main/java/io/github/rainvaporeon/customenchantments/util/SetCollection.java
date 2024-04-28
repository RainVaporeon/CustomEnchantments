package io.github.rainvaporeon.customenchantments.util;

import java.util.Objects;
import java.util.Set;

public class SetCollection {
    public static <T> T find(Set<T> set, T object) {
        return set.stream().filter(t -> Objects.equals(t, object)).findFirst().orElse(null);
    }
}
