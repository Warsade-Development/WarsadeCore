package com.warsade.core.utils.expirable;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExpirableMap<K, V> extends ConcurrentHashMap<K, V> {

    private static final long serialVersionUID = 1L;

    Thread expireThread;

    private final Map<K, Long> timeMap = new ConcurrentHashMap<>();
    private final ExpirableMapCallback<K, V> callback;
    private final long expiryInMillis;
    private boolean isAlive = false;

    public ExpirableMap(Long expiryInMillis, ExpirableMapCallback<K, V> callback) {
        this.expiryInMillis = expiryInMillis;
        this.callback = callback;
    }

    void initialize() {
        isAlive = true;

        expireThread = new ExpireThread();
        expireThread.start();
    }

    public Long getRemainingTime(K key) {
        if (!timeMap.containsKey(key)) return 0L;

        long currentTime = new Date().getTime();
        return (timeMap.get(key) + expiryInMillis) - currentTime;
    }

    @Override
    public V put(K key, V value) {
        if (!isAlive()) {
            initialize();
        }
        Date date = new Date();
        timeMap.put(key, date.getTime());

        V returnVal = super.put(key, value);

        return returnVal;
    }

    @Override
    public boolean containsKey(Object key) {
        long currentTime = new Date().getTime();
        if (timeMap.containsKey(key)) {
            if (((timeMap.get(key) + expiryInMillis) - currentTime) < 0L) {
                timeMap.remove(key);
                remove(key);
            }
        }

        return super.containsKey(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (!isAlive()) {
            initialize();
        }
        for (K key : m.keySet()) {
            put(key, m.get(key));
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        if (!isAlive()) {
            initialize();
        }

        if (!containsKey(key)) {
            return put(key, value);
        } else {
            return get(key);
        }
    }

    @Override
    public V remove(Object key) {
        V value = super.remove(key);
        timeMap.remove(key);

        callback.onRemove((K) key, value);

        if (isEmpty()) isAlive = false;
        return value;
    }

    public boolean isAlive() {
        return isAlive;
    }

    class ExpireThread extends Thread {

        @Override
        public void run() {
            while (isAlive) {
                expireValues();

                try {
                    Thread.sleep(expiryInMillis / 2);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        }

        private void expireValues() {
            long currentTime = new Date().getTime();
            for (K key : timeMap.keySet()) {
                if (((timeMap.get(key) + expiryInMillis) - currentTime) < 0L) {
                    remove(key);
                }
            }
        }
    }

}
