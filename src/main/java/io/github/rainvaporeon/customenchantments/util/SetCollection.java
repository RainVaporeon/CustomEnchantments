package io.github.rainvaporeon.customenchantments.util;

import io.github.rainvaporeon.customenchantments.util.infusions.InfusionInfo;

import java.util.*;

public class SetCollection {
    public static <T> T find(Set<T> set, T object) {
        return find(set, object, null);
    }

    public static <T> T findOrSelf(Set<T> set, T self) {
        return find(set, self, self);
    }

    public static <T> T find(Set<T> set, T object, T def) {
        return set.stream().filter(t -> Objects.equals(t, object)).findFirst().orElse(def);
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

    @SafeVarargs
    public static <T> Set<T> of(T... t) {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(t)));
    }
}
