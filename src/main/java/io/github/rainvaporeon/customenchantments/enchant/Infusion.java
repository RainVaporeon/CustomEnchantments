package io.github.rainvaporeon.customenchantments.enchant;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import io.github.rainvaporeon.customenchantments.util.SharedConstants;
import io.github.rainvaporeon.customenchantments.util.StringStyle;
import io.github.rainvaporeon.customenchantments.util.enums.InfusionTarget;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Predicate;

public abstract class Infusion {

    /**
     * Gets the identifier for this infusion type, used to allow
     * end users to specify infusion name.
     * @return the infusion identifier
     */
    public abstract String getIdentifier();

    /**
     * Gets the name for this infusion type
     * @return the name
     * @see Infusion#getDisplayName(int)
     * @apiNote If the display name is not implemented, the level
     * of the infusion is appended after the return value of this method.
     */
    public abstract String getName();

    /**
     * Gets the display name for this infusion type
     * @return the name that is shown on the item.
     * @implNote If a custom level position is required,
     * substitute the level position with {@code %s}.
     * @see Infusion#getName()
     */
    public String getDisplayName(int level) {
        return this.getName() + " %s";
    }

    /**
     * Gets the formatter for the display level
     * @return the display style
     */
    public Int2ObjectFunction<String> getLevelStyle() {
        return StringStyle::toRomanNumerals;
    }

    public TextColor getColor() {
        return NamedTextColor.GRAY;
    }

    /**
     * Gets the pattern to clear the lore this applies
     * @return the predicate
     */
    public Predicate<Component> getClearingPattern() {
        return s -> {
            try {
                if (s instanceof TextComponent) {
                    return ((TextComponent) s).content().startsWith(this.getName());
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        };
    }

    /**
     * Retrieves the listener instance for this infusion
     * @return the listener
     * @implNote If a listener is not required, use the empty
     * listener instance in {@link SharedConstants#emptyListener()}
     * instead.
     */
    public abstract @Nonnull Listener getListener();

    /**
     * Whether this infusion should be shown in /currentinfusion
     * @return whether this infusion should be shown in infusion summary
     * @apiNote if this is {@code false}, the extended description
     * will be used instead during the infusion list.
     * @see Infusion#getExtendedDescription(int)
     */
    public boolean showInSummary() {
        return true;
    }

    /**
     * Gets the infusion's description
     * @return the infusion description; cannot be null
     * @see Infusion#getExtendedDescription(int)
     */
    public String getDescription() {
        return "";
    }

    /**
     * Gets extended description for this infusion at given level
     * @param level the level
     * @return describing this infusion's functionality at this level
     */
    public @Nullable String getExtendedDescription(int level) {
        return null;
    }

    /**
     * Gets the maximum level for this infusion
     * @return the max level
     */
    public abstract int getMaxLevel();

    /**
     * Gets the maximum effective level for this infusion
     * @return the maximum effective level
     * @implNote when implementing the level cap, it'll be
     * important to also edit {@link Infusion#getExtendedDescription(int)}
     * accordingly, as well as handle the capped numbers separately.
     */
    public int getMaxEffectiveLevel() {
        return Integer.MAX_VALUE;
    }

    public ReadWriteNBT getNBT(int level) {
        ReadWriteNBT compound = NBT.createNBTObject();
        compound.setString(SharedConstants.INFUSION_ID, this.getIdentifier());
        compound.setInteger(SharedConstants.INFUSION_LEVEL, level);
        return compound;
    }

    public @Unmodifiable Set<EquipmentSlot> applicableSlots() {
        return SharedConstants.equipmentSlots();
    }

    /**
     * The target of this infusion
     * @return a set of target this infusion may be applied on
     */
    public @Unmodifiable Set<InfusionTarget> infusionTarget() {
        return EnumSet.of(InfusionTarget.ALL);
    }

    public final boolean isDebuff() { return this instanceof DebuffInfusion; }
    public final boolean isSpecial() { return this instanceof SpecialInfusion; }
    public final boolean isSet() { return this instanceof SetInfusion; }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Infusion) return this.getIdentifier().equals(((Infusion) obj).getIdentifier());
        return false;
    }

    @Override
    public int hashCode() {
        return this.getIdentifier().hashCode();
    }

    @Override
    public String toString() {
        return this.getName() + " (" + this.getIdentifier() + ")";
    }
}
