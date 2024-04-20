package io.github.rainvaporeon.customenchantments.enchant;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import io.github.rainvaporeon.customenchantments.util.StringStyle;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Predicate;

public abstract class Infusion {
    public static final String INFUSION_ID = "id";
    public static final String INFUSION_LEVEL = "lvl";

    /**
     * Gets the identifier for this infusion type, used to allow
     * end users to specify infusion name.
     * @return the infusion identifier
     */
    public abstract String getIdentifier();

    /**
     * Gets the name for this infusion type
     * @return the name
     * @see Infusion#getDisplayName()
     * @apiNote If display name is not implemented, the level
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
    public String getDisplayName() {
        return this.getName() + " %s";
    }

    /**
     * Gets the formatter for the display level
     * @return the display style
     */
    public Int2ObjectFunction<String> getLevelStyle() {
        return StringStyle::toRomanNumerals;
    }

    public NamedTextColor getColor() {
        return NamedTextColor.GRAY;
    }

    /**
     * Gets the pattern to clear the lore this applies
     * @return the predicate
     */
    public Predicate<String> getClearingPattern() {
        return s -> {
            try {
                JsonObject o = JsonParser.parseString(s).getAsJsonObject();
                return o.get("text").getAsString().startsWith(this.getName());
            } catch (Exception e) {
                return false;
            }
        };
    }

    public @Nullable Listener getListener() {
        return null;
    }

    public String getDescription() {
        return "";
    }

    public String getDescription(CommandSender sender) {
        return "";
    }

    public abstract int getMaxLevel();

    public ReadWriteNBT getNBT(int level) {
        ReadWriteNBT compound = NBT.createNBTObject();
        compound.setString(INFUSION_ID, this.getIdentifier());
        compound.setInteger(INFUSION_LEVEL, level);
        return compound;
    }

    public Set<EquipmentSlot> applicableSlots() {
        return EnumSet.allOf(EquipmentSlot.class);
    }

    /**
     * The target of this infusion
     * @return a set of target this infusion may be applied on
     */
    public Set<EnchantmentTarget> infusionTarget() {
        return EnumSet.allOf(EnchantmentTarget.class);
    }

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
        return this.getName() + "(" + this.getIdentifier() + ")";
    }
}
