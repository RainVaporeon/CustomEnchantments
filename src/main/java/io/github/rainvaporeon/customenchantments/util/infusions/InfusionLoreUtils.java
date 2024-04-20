package io.github.rainvaporeon.customenchantments.util.infusions;

import com.google.gson.JsonObject;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTList;
import de.tr7zw.nbtapi.NBTListCompound;
import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public final class InfusionLoreUtils {
    public static void applyLore(ItemStack stack, Infusion infusion, int level) {
        ItemMeta meta = stack.getItemMeta();
        if (!meta.hasLore()) meta.lore(new ArrayList<>());
        String display = infusion.getDisplayName();
        String lore = display.replace("%s", infusion.getLevelStyle().apply(level));
        meta.lore().add(Component.text().color(NamedTextColor.GRAY).content(lore).build());
        stack.setItemMeta(meta);
    }

    public static void removeLore(ItemStack stack, Infusion infusion) {
        ItemMeta meta = stack.getItemMeta();
        if (!meta.hasLore()) return;
        String display = infusion.getDisplayName().replace("%s", "");
        meta.lore().removeIf(component -> component.toString().contains(display));
        stack.setItemMeta(meta);
    }

    public static void applyLoreNBT(NBTItem item, Infusion infusion, int level) {
        NBTList<String> lore = item.getOrCreateCompound("display").getStringList("Lore");
        String display = infusion.getDisplayName();
        String value = display.replace("%s", infusion.getLevelStyle().apply(level));
        JsonObject o = new JsonObject();
        o.addProperty("text", value);
        o.addProperty("color", "gray");
        o.addProperty("italic", false);
        lore.addLast(o.toString());
    }

    public static void removeLoreNBT(NBTItem item, Infusion infusion) {
        NBTList<String> lore = item.getOrCreateCompound("display").getStringList("Lore");
        String display = infusion.getDisplayName();
        String value = display.replace("%s", "");
        lore.removeIf(content -> content.contains(value));
    }
}
