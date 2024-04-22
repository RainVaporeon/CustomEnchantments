package io.github.rainvaporeon.customenchantments.enchant.debuff;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import net.kyori.adventure.text.format.NamedTextColor;

public abstract class DebuffInfusion extends Infusion {
    @Override
    public NamedTextColor getColor() {
        return NamedTextColor.RED;
    }
}
