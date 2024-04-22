package io.github.rainvaporeon.customenchantments.util.particles;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;

public class Sounds {
    public static void playBlockBreakSound(Entity source) {
        source.getWorld().playSound(source.getLocation(),
                Sound.BLOCK_STONE_BREAK,
                1,
                1);
    }
}
