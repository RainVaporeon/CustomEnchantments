package io.github.rainvaporeon.customenchantments.enchant;

import io.github.rainvaporeon.customenchantments.util.SharedConstants;
import io.github.rainvaporeon.customenchantments.util.enums.InfusionTarget;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionInfo;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Base class denoting an infusion triggered with
 * multiple pieces of items equipped and provides different
 * effects depending on the amounts equipped.
 *
 * @apiNote All infusions under this category must have a maximum
 * level of 1, and should use {@link io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils#countInfusionPieces(Player, Infusion)}
 * to count the pieces of gears worn with given infusion.
 */
public abstract class SetInfusion extends Infusion {

    public abstract String getSetName();

    @Override
    public final String getName() {
        return this.getSetName() + " Set";
    }

    @Override
    public NamedTextColor getColor() {
        return NamedTextColor.GREEN;
    }

    @Override
    public final int getMaxLevel() {
        return 1;
    }

    /**
     * Gets the total infusion bonus for given set pieces equipped
     * @param level the pieces equipped that contain this set infusion
     * @return a set indicating all the infusions to apply
     */
    public Set<InfusionInfo> getInfusionBonuses(int level) {
        return Collections.emptySet();
    }

    /**
     * Applies the set bonuses to the player
     * @param player the player to apply to
     * @param level the level of infusion
     * @apiNote this method should only be called by the plugin logic
     * itself.
     */
    public void applySetBonus(Player player, int level) {

    }

    /**
     * Returns the maximum effective equipped pieces that
     * would unlock this set's maximum capability.
     * @return the max accumulated effective level
     * @implNote by default, the return value refers to the
     * size of {@link Infusion#applicableSlots()}, implementations
     * of this class may decide to insert a value later on.
     */
    @Override
    public int getMaxEffectiveLevel() {
        return this.applicableSlots().size();
    }
}
