package io.github.rainvaporeon.customenchantments.util.infusions;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class InfusionManager {

    private static final Set<Infusion> infusions = new HashSet<>();

    public static boolean registerInfusion(Infusion base) {
        return infusions.add(base);
    }

    public static boolean deregisterInfusion(Infusion base) {
        return infusions.remove(base);
    }

    public static List<Infusion> getInfusions() {
        return List.copyOf(infusions);
    }

    public static List<String> getInfusionIdentifiers() {
        return infusions.stream().map(Infusion::getIdentifier).toList();
    }

    public static @Nullable Infusion getInfusionByName(String name) {
        return infusions.stream().filter(i -> i.getName().equals(name)).findFirst().orElse(null);
    }

    public static @Nullable Infusion getInfusionById(String identifier) {
        return infusions.stream().filter(i -> i.getName().equals(identifier)).findFirst().orElse(null);
    }
}
