package io.github.rainvaporeon.customenchantments.init;

import io.github.rainvaporeon.customenchantments.util.enums.InfusionTarget;
import io.github.rainvaporeon.customenchantments.util.internal.misc.InfusionTargetFactory;

public interface Test {
    static void main(String[] args) {
        InfusionTarget[] a = InfusionTarget.values();
        InfusionTarget h = InfusionTargetFactory.newTarget(material -> false);
        for (Object o : a) if (o.equals(h)) System.out.print("N");
        System.out.print('K');
    }
}
