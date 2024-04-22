package io.github.rainvaporeon.customenchantments.enchant;

import net.kyori.adventure.text.format.NamedTextColor;

public abstract class DebuffInfusion extends Infusion {
    @Override
    public NamedTextColor getColor() {
        return NamedTextColor.RED;
    }
}
