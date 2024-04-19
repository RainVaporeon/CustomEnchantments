package io.github.rainvaporeon.customenchantments.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TabCompletionUtils {

    public static List<String> startsWith(String string, String... candidates) {
        return Arrays.stream(candidates).filter(id -> id.startsWith(string)).toList();
    }

    public static List<String> startsWith(String string, Collection<String> candidates) {
        return candidates.stream().filter(id -> id.startsWith(string)).toList();
    }
}
