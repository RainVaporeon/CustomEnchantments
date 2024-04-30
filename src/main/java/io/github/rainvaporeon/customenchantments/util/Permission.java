package io.github.rainvaporeon.customenchantments.util;

import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import org.bukkit.NamespacedKey;

public class Permission {

    public static NamespacedKey namespace(String id) {
        return new NamespacedKey(CustomEnchantments.PLUGIN, id);
    }

    public static String of(String id) {
        return "customenchantments." + id;
    }

    public static org.bukkit.permissions.Permission bukkit(String context) {
        return new org.bukkit.permissions.Permission(context);
    }
}
