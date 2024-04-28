package io.github.rainvaporeon.customenchantments.util;

import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.EnumSet;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.*;

public class SharedConstants {
    public static final String INFUSION_IDENTIFIER_KEY = "CEInfusions";
    public static final String STORED_INFUSION_IDENTIFIER_KEY = "CEStoredInfusions";
    public static final String ENTITY_EXTRA_DATA = "CEEntityExtras";
    public static final NamespacedKey INFUSION_NAMESPACE = new NamespacedKey(CustomEnchantments.PLUGIN, INFUSION_IDENTIFIER_KEY);
    public static final String INFUSION_ID = "id";
    public static final String INFUSION_LEVEL = "lvl";
    private static final Listener EMPTY = new Listener() {};

    private static final EnumSet<EntityDamageEvent.DamageCause> naturalCauses = EnumSet.of(
            FIRE, FIRE_TICK, LAVA,
            FALL,
            VOID,
            STARVATION,
            CUSTOM,
            HOT_FLOOR,
            DRYOUT,
            FREEZE,
            WITHER,
            POISON,
            MAGIC,
            SUICIDE,
            MELTING,
            DROWNING,
            SUFFOCATION,
            KILL,
            WORLD_BORDER
    );

    private static final EnumSet<EntityDamageEvent.DamageCause> unnaturalCauses = EnumSet.complementOf(naturalCauses);

    public static Listener emptyListener() {
        return EMPTY;
    }

    public static EnumSet<EntityDamageEvent.DamageCause> naturalCauses() {
        return naturalCauses;
    }

    public static EnumSet<EntityDamageEvent.DamageCause> unnaturalCauses() {
        return unnaturalCauses;
    }
}
