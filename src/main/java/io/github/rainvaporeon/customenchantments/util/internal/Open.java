package io.github.rainvaporeon.customenchantments.util.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicating that this class is made under the assumption
 * that it will be publicly used, and is therefore typically
 * ensured that there are no issues with using them in the
 * plugin.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Open {
}
