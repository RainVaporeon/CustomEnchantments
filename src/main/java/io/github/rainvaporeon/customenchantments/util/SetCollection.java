package io.github.rainvaporeon.customenchantments.util;

import io.github.rainvaporeon.customenchantments.util.infusions.InfusionInfo;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SetCollection {
    public static <T> T find(Set<T> set, T object) {
        return set.stream().filter(t -> Objects.equals(t, object)).findFirst().orElse(null);
    }

    /**
     * Forcibly adds an element to the set, removing the originally mapped key if needed
     *
     * @param set    the set
     * @param object the object
     * @param <T>    the type
     */
    public static <T> void addForced(Set<T> set, T object) {
        if (set.contains(object)) {
            set.remove(object);
            set.add(object);
            return;
        }
        set.add(object);
    }

    @SafeVarargs
    public static Set<InfusionInfo> mergeInfo(Set<InfusionInfo>... infos) {
        Set<InfusionInfo> ret = new HashSet<>();
        for (Set<InfusionInfo> info : infos) {
            info.forEach(i -> {
                if (ret.contains(i)) {
                    InfusionInfo replace = SetCollection.find(info, i).combine(i);
                    ret.remove(i);
                    ret.add(replace);
                } else {
                    ret.add(i);
                }
            });
        }
        return ret;
    }

    public static Set<InfusionInfo> mergeInfo(Iterable<Set<InfusionInfo>> infos) {
        Set<InfusionInfo> ret = new HashSet<>();
        for (Set<InfusionInfo> info : infos) {
            info.forEach(i -> {
                if (ret.contains(i)) {
                    InfusionInfo replace = SetCollection.find(info, i).combine(i);
                    ret.remove(i);
                    ret.add(replace);
                } else {
                    ret.add(i);
                }
            });
        }
        return ret;
    }
}
