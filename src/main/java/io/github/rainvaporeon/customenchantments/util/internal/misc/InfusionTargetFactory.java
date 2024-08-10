package io.github.rainvaporeon.customenchantments.util.internal.misc;

import io.github.rainvaporeon.customenchantments.util.enums.InfusionTarget;
import io.github.rainvaporeon.customenchantments.util.internal.Open;
import io.github.rainvaporeon.customenchantments.util.internal.exceptions.InvocationException;
import io.github.rainvaporeon.customenchantments.util.internal.unstable.ClassTools;
import org.bukkit.Material;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Predicate;

@ApiStatus.Internal @Open
public final class InfusionTargetFactory {
    /**
     * Instantiates a new infusion target enum.
     * The newly created enum is nameless, has an ordinal of 999, and
     * follows the test as the predicate provides
     * @param p the predicate
     * @return a new instance of the enum
     * @apiNote It is <b>strongly encouraged</b> to keep the constructed enum
     * instance elsewhere, it should be considered as a new enum instance and
     * similar to other enums, should never be constructed more than once.
     */
    public static InfusionTarget newTarget(Predicate<Material> p) {
        try {
            return ClassTools.instantiateClass(InfusionTarget.class, new Class[] {String.class, int.class, Predicate.class}, "" , 999, p);
        } catch (InvocationException e) {
            throw new InternalError(e);
        }
    }
}