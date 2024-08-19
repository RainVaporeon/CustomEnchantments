package io.github.rainvaporeon.customenchantments.util.internal.accessors;

public class CESecrets {
    private static InfusionCacheAccessor infusionCacheAccessor;

    public static void setInfusionCacheAccessor(InfusionCacheAccessor infusionCacheAccessor) {
        CESecrets.infusionCacheAccessor = infusionCacheAccessor;
    }

    public static InfusionCacheAccessor getInfusionCacheAccessor() {
        return infusionCacheAccessor;
    }
}
