package io.github.rainvaporeon.customenchantments.util.collections;

import it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TimedHashSet<E> implements Set<E> {
    private final long lifetime;
    private final Object2LongArrayMap<E> timer;

    public TimedHashSet(long lifetime) {
        this.lifetime = lifetime;
        this.timer = new Object2LongArrayMap<>();
    }

    public TimedHashSet(long lifetime, int initialCapacity) {
        this.lifetime = lifetime;
        this.timer = new Object2LongArrayMap<>(initialCapacity);
    }

    @Override
    public int size() {
        clean();
        return timer.size();
    }

    @Override
    public boolean isEmpty() {
        clean();
        return timer.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        clean();
        if (timer.containsKey(o)) {
            return System.currentTimeMillis() <= timer.getLong(o);
        }
        return false;
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        clean();
        return timer.keySet().iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        clean();
        return timer.keySet().toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        clean();
        return (T[]) timer.keySet().toArray();
    }

    @Override
    public boolean add(E e) {
        clean();
        return timer.put(e, System.currentTimeMillis() + lifetime) != 0;
    }

    @Override
    public boolean remove(Object o) {
        clean();
        if (!timer.containsKey(o)) return false;
        timer.remove(o, timer.getLong(o));
        return true;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        clean();
        return timer.keySet().containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        clean();
        int size = size();
        c.forEach(e -> timer.put(e, System.currentTimeMillis() + lifetime));
        return size != size();
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        clean();
        return timer.keySet().retainAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return timer.keySet().removeAll(c);
    }

    @Override
    public void clear() {
        timer.clear();
    }

    private void clean() {
        timer.object2LongEntrySet().removeIf(e -> e.getLongValue() < System.currentTimeMillis());
    }
}
