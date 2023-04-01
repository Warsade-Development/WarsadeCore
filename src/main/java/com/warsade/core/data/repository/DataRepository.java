package com.warsade.core.data.repository;

import java.util.List;
import java.util.Optional;

public interface DataRepository <K, V> {

    Optional<V> get(K key);

    void create(V data);
    void delete(V data);

    void save(V data);

    List<V> getData();
    int getDataAmount();

}
