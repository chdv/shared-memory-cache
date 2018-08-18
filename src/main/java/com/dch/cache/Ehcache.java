package com.dch.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;

public class Ehcache implements ICache {

    private Cache<Long, byte[]> cache;
    private CacheManager cacheManager;

    private static final String CACHE_NAME = "sample-offheap-cache";

    public Ehcache(long offHeap) {
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache(CACHE_NAME,
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                Long.class,
                                byte[].class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder().offheap(
                                        offHeap,
                                        MemoryUnit.B)
                        )
                ).build();

        cacheManager.init();

        cache = cacheManager.getCache(CACHE_NAME, Long.class, byte[].class);
    }

    @Override
    public byte[] get(long key) {
        return cache.get(key);
    }

    @Override
    public boolean put(long key, byte[] value) {
        cache.put(key, value);
        return true;
    }

    @Override
    public void close() {
        cacheManager.removeCache(CACHE_NAME);
        cacheManager.close();
    }
}
