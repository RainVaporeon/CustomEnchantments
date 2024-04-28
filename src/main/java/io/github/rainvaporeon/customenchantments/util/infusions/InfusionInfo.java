package io.github.rainvaporeon.customenchantments.util.infusions;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;

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
}
