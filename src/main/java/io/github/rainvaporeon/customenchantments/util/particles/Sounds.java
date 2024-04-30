package io.github.rainvaporeon.customenchantments.util.particles;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;

public class Sounds {
    public static void playBlockBreakSound(Entity source) {
        playSound(source, Sound.BLOCK_STONE_BREAK);
    }

    public static void playSplinterEffectSound(Entity source) {
        playSound(source, Sound.BLOCK_GLASS_BREAK, 0.75F, 2F);
    }

    public static void playSound(Entity source, Sound sound) {
        source.getWorld().playSound(
                source.getLocation().add(0, source.getHeight() / 2, 0),
                sound,
                1,
                1);
    }

    public static void playSound(Entity source, Sound sound, float volume) {
        source.getWorld().playSound(
                source.getLocation().add(0, source.getHeight() / 2, 0),
                sound,
                volume,
                1);
    }

    public static void playSound(Entity source, Sound sound, float volume, float pitch) {
        source.getWorld().playSound(
                source.getLocation().add(0, source.getHeight() / 2, 0),
                sound,
                volume,
                pitch);
    }
}
