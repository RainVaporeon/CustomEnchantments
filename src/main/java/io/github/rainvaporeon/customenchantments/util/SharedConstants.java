package io.github.rainvaporeon.customenchantments.util;

import org.bukkit.event.Listener;

public class SharedConstants {
    private static final Listener EMPTY = new Listener() {};

    public static Listener emptyListener() {
        return EMPTY;
    }
}
