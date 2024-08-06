package io.github.rainvaporeon.customenchantments.util.infusions;

import de.tr7zw.nbtapi.NBT;
import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class InfusionLoreUtils {
    public static void applyLoreNBT(ItemMeta item, Infusion infusion, int level) {
        String display = infusion.getDisplayName(level);
        String value = display.replace("%s", infusion.getLevelStyle().apply(level));
        Component text = Component.text(value).color(infusion.getColor());
        Style style = text.style().decoration(TextDecoration.ITALIC, false);
        Component result = text.style(style);
        List<Component> lore = item.hasLore() ? item.lore() : new ArrayList<>();
        if (item.lore() == null) {
            lore.add(result);
        } else {
            lore.addLast(result);
        }
        item.lore(lore);
    }

    /**
     * @apiNote only use this under {@link NBT#modify(ItemStack, Consumer)}
     */
    public static void removeLoreNBT(ItemMeta item, Infusion infusion) {
        List<Component> lore = item.hasLore() ? item.lore() : new ArrayList<>();
        lore.removeIf(c -> infusion.getClearingPattern().test(c));
        item.lore(lore);
    }

    /**
     * @apiNote only use this under {@link NBT#modify(ItemStack, Consumer)}
     */
    private static void removeAllLoreNBT(ItemMeta item) {
        List<Component> lore = item.hasLore() ? item.lore() : new ArrayList<>();
        lore.removeIf(c -> {
            for (Infusion infusion : InfusionManager.getInfusions()) {
                if (infusion.getClearingPattern().test(c)) return true;
            }
            return false;
        });
        item.lore(lore);
    }

    public static ItemStack applySortedLoreNBT(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        removeAllLoreNBT(meta);
        for (Infusion infusion : InfusionManager.getInfusions()) {
            int level = InfusionUtils.getInfusion(stack, infusion);
            if (level != 0) {
                applyLoreNBT(meta, infusion, level);
            }
            int storedLevel = InfusionUtils.getStoredInfusion(stack, infusion);
            if (storedLevel != 0) {
                applyLoreNBT(meta, infusion, storedLevel);
            }
        }
        stack.setItemMeta(meta);
        return stack;
    }
}
