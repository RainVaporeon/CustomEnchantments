package io.github.rainvaporeon.customenchantments.enchant;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

/**
 * Base class denoting an infusion triggered with
 * multiple pieces of items equipped and provides different
 * effects depending on the amounts equipped.
 *
 * @apiNote All infusions under this category must have a maximum
 * level of 1, and should use {@link io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils#accumulateEquippedPiecesWithInfusion(Player, Infusion)}
 * to count the pieces of gears worn with given infusion.
 */
public abstract class SetInfusion extends Infusion {

    @Override
    public NamedTextColor getColor() {
        return NamedTextColor.GREEN;
    }

    @Override
    public final int getMaxLevel() {
        return 1;
    }

}
