package io.github.rainvaporeon.customenchantments.util.infusions;

import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.SharedConstants;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import javax.annotation.Nullable;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class InfusionManager {

    // We use LinkedHashSet here as it's ordered, and we put the infusions in declaration order,
    // making the lore ordered and for other display purposes.
    private static final LinkedHashSet<Infusion> infusions = new LinkedHashSet<>();

    /**
     * Registers the infusion to the manager
     * @param base the infusion
     * @throws IllegalArgumentException if the identifier contains spaces,
     * or if the infusion already has been registered, or if an infusion with
     * the same identifier exists already
     */
    public static void registerInfusion(Infusion base) {
        validateInfusion(base);
        Listener listener = base.getListener();
        if (listener != SharedConstants.emptyListener()) {
            Bukkit.getPluginManager().registerEvents(listener, CustomEnchantments.PLUGIN);
        }
        CustomEnchantments.PLUGIN.getLogger().log(Level.INFO, "Registered infusion " + base);
        infusions.addLast(base);
    }

    /**
     * Bulk registers infusions
     * @param infusions infusions
     * @see InfusionManager#registerInfusion(Infusion)
     */
    public static void registerInfusions(Infusion... infusions) {
        for (Infusion infusion : infusions) registerInfusion(infusion);
    }

    /**
     * Gets the currently registered infusions.
     * @return a copy of the infusions
     */
    public static Set<Infusion> getInfusions() {
        return new LinkedHashSet<>(infusions);
    }

    /**
     * Gets all registered infusion identifiers
     * @return a list of valid identifiers that can be translated into an infusion
     */
    public static List<String> getInfusionIdentifiers() {
        return infusions.stream().map(Infusion::getIdentifier).collect(Collectors.toList());
    }

    /**
     * Gets an infusion by its identifier
     * @param identifier the identifier
     * @return the infusion, or null if absent
     */
    public static @Nullable Infusion getInfusionById(String identifier) {
        return infusions.stream().filter(i -> i.getIdentifier().equals(identifier)).findFirst().orElse(null);
    }

    /**
     * Validates the infusion
     * @param infusion the infusion
     * @throws IllegalArgumentException if infusion contains a space in its identifier,
     * or if already registered, or has a conflicting identifier
     */
    private static void validateInfusion(Infusion infusion) {
        if (infusion.getIdentifier().contains(" ")) throw new IllegalArgumentException("infusion " + infusion + " has illegal identifier " + infusion.getIdentifier());
        if (infusions.contains(infusion)) throw new IllegalArgumentException("re-registered " + infusion + " twice!");
        Infusion duplicate = getInfusionById(infusion.getIdentifier());
        if (duplicate != null) {
            throw new IllegalArgumentException("Infusion " + infusion + " has duplicate ID with " + duplicate + "!");
        }
    }
}
