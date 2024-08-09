package io.github.rainvaporeon.customenchantments.util.internal.misc;

import io.github.rainvaporeon.customenchantments.util.enums.InfusionTarget;
import io.github.rainvaporeon.customenchantments.util.internal.Open;
import io.github.rainvaporeon.customenchantments.util.internal.exceptions.InvocationException;
import io.github.rainvaporeon.customenchantments.util.internal.unstable.EnumFactory;
import org.bukkit.Material;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Predicate;

@ApiStatus.Internal @Open
public final class InfusionTargetFactory {
    public static InfusionTarget newTarget(Predicate<Material> p) {
        try {
            return EnumFactory.instantiateEnum(InfusionTarget.class, new Class[] {String.class, int.class, Predicate.class}, "" , 999, p);
        } catch (InvocationException e) {
            throw new InternalError(e);
        }
    }
}
