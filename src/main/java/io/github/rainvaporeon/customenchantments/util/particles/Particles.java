package io.github.rainvaporeon.customenchantments.util.particles;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

public class Particles {
    public static void playBleedingParticle(Entity source) {
        source.getWorld().spawnParticle(Particle.ITEM_CRACK,
                source.getLocation(),
                10,
                Material.REDSTONE_BLOCK);
    }
}
