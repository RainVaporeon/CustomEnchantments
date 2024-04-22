package io.github.rainvaporeon.customenchantments.util.infusions;

import com.google.gson.JsonObject;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTList;
import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.logging.Level;

public final class InfusionLoreUtils {
    public static void applyLoreNBT(NBTItem item, Infusion infusion, int level) {
        NBTList<String> lore = item.getOrCreateCompound("display").getStringList("Lore");
        String display = infusion.getDisplayName();
        String value = display.replace("%s", infusion.getLevelStyle().apply(level));
        JsonObject o = new JsonObject();
        o.addProperty("text", value);
        o.addProperty("color", infusion.getColor().toString());
        o.addProperty("italic", false);
        lore.addLast(o.toString());
    }

    public static void removeLoreNBT(NBTItem item, Infusion infusion) {
        NBTList<String> lore = item.getOrCreateCompound("display").getStringList("Lore");
        lore.removeIf(c -> {
            CustomEnchantments.PLUGIN.getLogger().log(Level.INFO, "Trying to remove lore line " + c + " for infusion " + infusion.getIdentifier());
            boolean ok = infusion.getClearingPattern().test(c);
            if (ok) {
                CustomEnchantments.PLUGIN.getLogger().log(Level.INFO, "Removed line " + c);
            }
            return ok;
        });
    }

    public static void applySortedLoreNBT(ItemStack stack) {
        NBTItem item = new NBTItem(stack);
        for (Map.Entry<Infusion, Integer> entry : InfusionUtils.getAllInfusions(stack).entrySet()) {
            removeLoreNBT(item, entry.getKey());
            applyLoreNBT(item, entry.getKey(), entry.getValue());
        }
        item.applyNBT(stack);
    }
}
