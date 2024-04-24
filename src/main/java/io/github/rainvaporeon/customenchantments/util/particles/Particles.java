package io.github.rainvaporeon.customenchantments.util.particles;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class Particles {
    public static void playBleedingParticle(Entity source) {
        source.getWorld().spawnParticle(Particle.ITEM_CRACK,
                source.getLocation().add(0, source.getHeight() / 2, 0),
                12,
                new ItemStack(Material.REDSTONE_BLOCK));
    }
}
