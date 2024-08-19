package io.github.rainvaporeon.customenchantments.util.internal.accessors;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionInfo;

public interface InfusionCacheAccessor {
    InfusionInfo get(Infusion type);
}
