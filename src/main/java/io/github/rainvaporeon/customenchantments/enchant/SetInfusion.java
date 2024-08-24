package io.github.rainvaporeon.customenchantments.enchant;

import io.github.rainvaporeon.customenchantments.util.SetCollection;
import io.github.rainvaporeon.customenchantments.util.SharedConstants;
import io.github.rainvaporeon.customenchantments.util.enums.InfusionTarget;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionInfo;
import io.github.rainvaporeon.customenchantments.util.internal.accessors.CESecrets;
import io.github.rainvaporeon.customenchantments.util.internal.accessors.InfusionCacheAccessor;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
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

    @Override
    public final String getDisplayName(int level) {
        return this.getName();
    }

    /**
     * Gets the total infusion bonus for given set pieces equipped
     * @param level the number of pieces worn with this infusion
     * @return a set indicating all the infusions to apply
     */
    public @Unmodifiable Set<InfusionInfo> getInfusionBonuses(int level) {
        return Collections.emptySet();
    }

    /**
     * Gets the infusion bonus on this infusion type
     * @param type the type
     * @param level the number of pieces worn with this infusion
     * @return the bonus levels on this infusion, or zero if absent or inapplicable.
     */
    public int getInfusionBonus(Infusion type, int level) {
        return SetCollection.find(getInfusionBonuses(level), CESecrets.getInfusionCacheAccessor().get(type)).getLevel();
    }

    /**
     * Applies the set bonuses to the player, this should only be
     * related to non-infusion related bonuses!
     * @param player the player to apply to
     * @param level the level of infusion
     * @param tick the tick, 1 to 20.
     * @apiNote this method should only be called by the plugin logic
     * itself. <p>
     *     This method is called every tick.
     * </p>
     * @implNote one should use {@link SetInfusion#getInfusionBonuses(int)} for
     * infusion-related bonuses.
     */
    public void applySetBonus(Player player, int level, int tick) {

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
