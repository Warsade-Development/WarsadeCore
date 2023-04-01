package com.warsade.core.utils.expirable;

public interface ExpirableMapCallback<K, V> {

    void onRemove(K key, V value);

}