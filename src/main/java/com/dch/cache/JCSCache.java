package com.dch.cache;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.access.exception.CacheException;

public class JCSCache implements ICache {

    private CacheAccess<Long, byte[]> cache = null;

    public JCSCache(String name) throws CacheException {
        this.cache = JCS.getInstance(name);
    }

    @Override
    public byte[] get(long key) {
        return (byte[]) cache.get(key);
    }

    @Override
    public boolean put(long key, byte[] value) {
        try {
            cache.put(key, value);
            return true;
        } catch (CacheException e) {
            return false;
        }
    }

    @Override
    public void close() {
        cache.dispose();
    }
}
