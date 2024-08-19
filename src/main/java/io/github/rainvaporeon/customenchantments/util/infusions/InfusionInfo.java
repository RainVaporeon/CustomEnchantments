package io.github.rainvaporeon.customenchantments.util.infusions;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.internal.accessors.CESecrets;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @apiNote The equality method in this class ignores the level.
 */
public class InfusionInfo {
    private final Infusion infusion;
    private final int level;

    public InfusionInfo(Infusion infusion, int level) {
        this.infusion = infusion;
        this.level = level;
    }

    public Infusion getInfusion() {
        return infusion;
    }

    public int getLevel() {
        return level;
    }

    public InfusionInfo combine(InfusionInfo other) {
        if (!this.equals(other)) throw new IllegalArgumentException("cannot combine different infusion type into one info");
        return new InfusionInfo(this.infusion, this.level + other.level);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfusionInfo that = (InfusionInfo) o;
        return Objects.equals(infusion, that.infusion);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(infusion);
    }

    @Override
    public String toString() {
        return this.infusion.getIdentifier() + "@Lv." + level;
    }

    static class Cache {
        private static final Map<Infusion, InfusionInfo> cache = new HashMap<>();

        static {
            CESecrets.setInfusionCacheAccessor(Cache::get);
        }

        static void fillInCache() {
            InfusionManager.getInfusions().forEach(i -> cache.put(i, new InfusionInfo(i, 0)));
        }

        static InfusionInfo get(Infusion type) { return cache.get(type); }
    }
}
