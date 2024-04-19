package io.github.rainvaporeon.customenchantments.util.infusions;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTListCompound;
import io.github.rainvaporeon.customenchantments.enchant.Infusion;

public final class InfusionLoreUtils {
    public static void applyLore(NBTCompoundList compound, Infusion infusion, int level) {
        String display = infusion.getDisplayName();
        String lore = display.replace("%s", infusion.getLevelStyle().apply(level));
        NBTListCompound listCompound = compound.addCompound();
        listCompound.setString("text", lore);
        listCompound.setString("color", "gray");
    }

    public static void removeLore(NBTCompoundList compoundList, Infusion infusion) {
        String display = infusion.getDisplayName().replace("%s", "");
        compoundList.removeIf(rw -> rw.getString("text").contains(display));
    }
}
