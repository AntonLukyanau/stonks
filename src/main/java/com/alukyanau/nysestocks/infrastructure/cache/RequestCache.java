package com.alukyanau.nysestocks.infrastructure.cache;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Set;

public class RequestCache<K, V> {

    //TODO replace to concurrent supported cache
    private final LRuCacheMap<K, SoftReference<V>> values;

    public RequestCache(int maxCacheSize) {
        values = new LRuCacheMap<>(maxCacheSize);
    }

    public int getMaxSize() {
        return values.getMaxSize();
    }

    public int size() {
        return values.size();
    }

    public boolean containsKey(K key) {
        reduceEmptyValues();
        return values.containsKey(key);
    }

    public V getBy(K key) {
        reduceEmptyValues();
        SoftReference<V> reference = values.get(key);
        return reference == null ? null : reference.get();
    }

    public void store(K key, V value) {
        reduceEmptyValues();
        SoftReference<V> reference = new SoftReference<>(value);
        values.put(key, reference);
    }

    private void reduceEmptyValues() {
        Set<Map.Entry<K, SoftReference<V>>> entries = values.entrySet();
        for (Map.Entry<K, SoftReference<V>> entry : entries) {
            if (entry.getValue().refersTo(null)) {
                values.remove(entry.getKey());
            }
        }
    }

}
