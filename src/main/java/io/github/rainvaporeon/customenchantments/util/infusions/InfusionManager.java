package io.github.rainvaporeon.customenchantments.util.infusions;

import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.enchant.SetInfusion;
import io.github.rainvaporeon.customenchantments.util.SharedConstants;
import io.github.rainvaporeon.customenchantments.util.collections.Pair;
import io.github.rainvaporeon.customenchantments.util.server.Server;
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

    private static final LinkedHashSet<SetInfusion> setInfusions = new LinkedHashSet<>();

    // Convenient type lookup
    private static final Map<Class<? extends Infusion>, Infusion> typeMap = new HashMap<>();

    // Convenient name lookup
    private static final Map<String, Infusion> queryMap = new HashMap<>();

    private static final Map<String, String> migrationMap = new HashMap<>();

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
        if (infusions.contains(base)) {
            CustomEnchantments.PLUGIN.getLogger().warning("Infusion " + base + " was already registered! Skipping this infusion...");
            Server.log(Level.WARNING, "Traceback: " + new IllegalArgumentException().fillInStackTrace());
            return;
        }
        Server.log(Level.INFO, "Registered infusion " + base);
        infusions.add(base);
        queryMap.put(base.getIdentifier(), base);
        typeMap.put(base.getClass(), base);
        if (base instanceof SetInfusion) {
            setInfusions.addLast((SetInfusion) base);
        }
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
     * Updates the underlying cache for the infusion info.
     * Should be called after finishing on registering all
     * infusions (or for the moment)
     */
    public static void updateCache() {
        InfusionInfo.Cache.fillInCache();
    }

    /**
     * Gets the currently registered infusions.
     * @return a copy of the infusions
     */
    public static Set<Infusion> getInfusions() {
        return new LinkedHashSet<>(infusions);
    }

    public static Set<SetInfusion> getSetInfusions() {
        return new LinkedHashSet<>(setInfusions);
    }

    public static void registerMigrationMapping(String from, String to) {
        migrationMap.put(from, to);
    }

    public static @Nullable String getMigrationMapping(String source) {
        return migrationMap.get(source);
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
        return queryMap.get(identifier);
    }

    /**
     * Gets an infusion by the type
     * @param infusion the infusion type
     * @return the registered infusion, or null if not registered
     */
    public static @Nullable Infusion getInfusionByType(Class<? extends Infusion> infusion) {
        return typeMap.get(infusion);
    }

    private static void callSetEffectHandlers(int tick) {
        for (SetInfusion infusion : setInfusions) {
            Server.collectPlayers(p -> InfusionUtils.hasInfusion(p, infusion))
                    .stream().map(p -> Pair.of(p, SetInfusionUtils.countActiveSetPieces(p, infusion)))
                    .forEach(p -> infusion.applySetBonus(p.key(), p.value(), tick));
        }
    }

    private static boolean enabled = false;
    public static void activeCycle() {
        if (enabled) {
            Server.warn("activeCycle() called twice");
            return;
        }
        enabled = true;
        activeCycle0();
    }

    private static void activeCycle0() {
        Server.runTaskRepeated(() -> callSetEffectHandlers(Server.getTimer()), 0, 1);
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
