package com.warsade.core.data.dao;

import java.util.HashMap;

public interface DataDao <K, V> {

    HashMap<K, V> loadAll();

    void create(V data);
    void delete(V data);

    void save(V data);

}
