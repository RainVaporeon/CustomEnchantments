package io.github.rainvaporeon.customenchantments.util.particles;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class Particles {
    public static void playBleedingParticle(Entity source) {
        Location halfHeight = source.getLocation().add(0, source.getHeight() / 2, 0);
        source.getWorld().spawnParticle(Particle.ITEM_CRACK,
                halfHeight.x(),
                halfHeight.y(),
                halfHeight.z(),
                6,
                0.25,
                0.25,
                0.25,
                0.5,
                new ItemStack(Material.REDSTONE_BLOCK));
    }
}
