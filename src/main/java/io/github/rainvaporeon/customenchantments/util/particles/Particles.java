package io.github.rainvaporeon.customenchantments.util.particles;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class Particles {
    public static void playBleedingParticle(Entity source) {
        playBlockBreakEffect(source, Material.REDSTONE_BLOCK);
    }

    public static void playPoisonParticle(Entity source) {
        playBlockBreakEffect(source, Material.SLIME_BLOCK);
    }

    public static void playSplinterParticle(Entity source) {
        playBlockBreakEffect(source, Material.GLASS);
    }

    public static void playBlockBreakEffect(Entity source, Material material) {
        Location halfHeight = source.getLocation().add(0, source.getHeight() / 2, 0);
        source.getWorld().spawnParticle(Particle.BLOCK,
                halfHeight.x(),
                halfHeight.y(),
                halfHeight.z(),
                6,
                0.25,
                0.25,
                0.25,
                0.3,
                new ItemStack(material));
    }
}
