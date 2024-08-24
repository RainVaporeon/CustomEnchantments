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
    static {
        CESecrets.setInfusionCacheAccessor(Cache::get);
    }

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

    public static InfusionInfo of(Class<? extends Infusion> infusion, int level) {
        return new InfusionInfo(InfusionManager.getInfusionByType(infusion), level);
    }

    public static InfusionInfo of(Infusion infusion, int level) {
        return new InfusionInfo(infusion, level);
    }

    public static InfusionInfo one(Infusion infusion) {
        return of(infusion, 1);
    }

    public static InfusionInfo two(Infusion infusion) {
        return of(infusion, 2);
    }

    public static InfusionInfo three(Infusion infusion) {
        return of(infusion, 3);
    }

    public static InfusionInfo four(Infusion infusion) {
        return of(infusion, 4);
    }

    public static InfusionInfo five(Infusion infusion) {
        return of(infusion, 5);
    }

    static class Cache {
        private static final Map<Infusion, InfusionInfo> cache = new HashMap<>();

        static void fillInCache() {
            InfusionManager.getInfusions().forEach(i -> cache.put(i, new InfusionInfo(i, 0)));
        }

        static InfusionInfo get(Infusion type) { return cache.get(type); }
    }
}
