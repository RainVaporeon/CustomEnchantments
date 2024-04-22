package io.github.rainvaporeon.customenchantments.util.infusions;

import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class InfusionManager {

    private static final Set<Infusion> infusions = new HashSet<>();

    public static boolean registerInfusion(Infusion base) {
        if (infusions.contains(base)) return false;
        validateInfusion(base);
        Listener listener = base.getListener();
        if (listener != null) Bukkit.getPluginManager().registerEvents(listener, CustomEnchantments.PLUGIN);
        CustomEnchantments.PLUGIN.getLogger().log(Level.INFO, "Registered infusion " + base);
        return infusions.add(base);
    }

    public static void registerInfusions(Infusion... infusions) {
        for (Infusion infusion : infusions) registerInfusion(infusion);
    }

    public static boolean deregisterInfusion(Infusion base) {
        return infusions.remove(base);
    }

    public static Set<Infusion> getInfusions() {
        return Collections.unmodifiableSet(infusions);
    }

    public static List<String> getInfusionIdentifiers() {
        return infusions.stream().map(Infusion::getIdentifier).collect(Collectors.toList());
    }

    public static @Nullable Infusion getInfusionByName(String name) {
        return infusions.stream().filter(i -> i.getName().equals(name)).findFirst().orElse(null);
    }

    public static @Nullable Infusion getInfusionById(String identifier) {
        return infusions.stream().filter(i -> i.getIdentifier().equals(identifier)).findFirst().orElse(null);
    }

    private static void validateInfusion(Infusion infusion) {
        if (infusion.getIdentifier().contains(" ")) throw new IllegalArgumentException("infusion " + infusion + " has illegal identifier " + infusion.getIdentifier());
        Infusion duplicate = getInfusionById(infusion.getIdentifier());
        if (duplicate != null) {
            throw new IllegalArgumentException("Infusion " + infusion + " has duplicate ID with " + duplicate + "!");
        }
    }
}
