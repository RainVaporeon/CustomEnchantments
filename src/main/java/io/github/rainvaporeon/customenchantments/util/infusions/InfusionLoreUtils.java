package io.github.rainvaporeon.customenchantments.util.infusions;

import com.google.gson.JsonObject;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTList;
import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import org.bukkit.inventory.ItemStack;

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
        lore.removeIf(c -> infusion.getClearingPattern().test(c));
    }

    private static void removeAllLoreNBT(NBTItem item) {
        NBTList<String> lore = item.getOrCreateCompound("display").getStringList("Lore");
        lore.removeIf(c -> {
            for (Infusion infusion : InfusionManager.getInfusions()) {
                if (infusion.getClearingPattern().test(c)) return true;
            }
            return false;
        });
    }

    public static void applySortedLoreNBT(ItemStack stack) {
        NBTItem item = new NBTItem(stack);
        removeAllLoreNBT(item);
        for (Infusion infusion : InfusionManager.getInfusions()) {
            int level = InfusionUtils.getInfusion(stack, infusion);
            if (level != 0) {
                applyLoreNBT(item, infusion, level);
            }
            int storedLevel = InfusionUtils.getStoredInfusion(stack, infusion);
            if (storedLevel != 0) {
                applyLoreNBT(item, infusion, level);
            }
        }

        item.applyNBT(stack);
    }
}
