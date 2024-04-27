package io.github.rainvaporeon.customenchantments.util;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.EnumSet;
import java.util.Set;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.*;

public class SharedConstants {
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
