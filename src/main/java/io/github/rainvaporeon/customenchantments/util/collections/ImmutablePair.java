package io.github.rainvaporeon.customenchantments.util.collections;

public class ImmutablePair<K, V> implements Pair<K, V> {
    private final K key;
    private final V value;

    public ImmutablePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }
}
