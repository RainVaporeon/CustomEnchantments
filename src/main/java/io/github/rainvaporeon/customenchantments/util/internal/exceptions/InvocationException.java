package io.github.rainvaporeon.customenchantments.util.internal.exceptions;

/**
 * Exception indicating that an exception had occurred whilst
 * invoking this method. This exception class may wrap {@link Error}
 * instances.
 */
public class InvocationException extends Exception {
    public InvocationException(String s, Throwable c) { super(s, c); }
    public InvocationException(Throwable c) { super(c); }
    public InvocationException(String s) { super(s); }
    public InvocationException() { super(); }
}
