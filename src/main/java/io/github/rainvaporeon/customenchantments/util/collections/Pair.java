package io.github.rainvaporeon.customenchantments.util.collections;

public interface Pair<K, V> {
    K key();

    V value();

    static <K, V> Pair<K, V> of(K k, V v) {
        return new ImmutablePair<>(k, v);
    }
}
