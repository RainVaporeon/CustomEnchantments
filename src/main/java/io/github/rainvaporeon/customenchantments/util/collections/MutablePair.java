package io.github.rainvaporeon.customenchantments.util.collections;

public class MutablePair<K, V> implements Pair<K, V> {
    private K key;
    private V value;

    public MutablePair(K key, V value) {
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

    public void key(K key) {
        this.key = key;
    }

    public void value(V value) {
        this.value = value;
    }
}
