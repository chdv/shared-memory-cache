package com.dch;

import com.dch.cache.*;

import java.util.Random;

/**
 *  Run on maven: mvn exec:java -Dexec.args='umc'
 *  Run on gradle: gradle run --args 'umc'
 */
public class CacheCompaire {
    private static final long K = 1024;
    private static final long M = 1024*K;
    private static final long G = 1024*M;

    private static final long MAGIC = 54331;

    private static final int WARMUP_COUNT = 100000;
    private static final int RUN_COUNT    = 1000000;

    public static void testWrite(ICache cache, int count) {
        Random random = new Random(0);
        for (int i = 0; i < count; i++) {
            long key = random.nextInt(1 << 20) * MAGIC;
            cache.put(key, new byte[random.nextInt(8192)]);
        }
    }

    public static void testRead(ICache cache, int count) {
        Random random = new Random(1);
        for (int i = 0; i < count; i++) {
            long key = random.nextInt(1 << 20) * MAGIC;
            cache.get(key);
        }
    }

    public static void testRead9Write1(ICache cache, int count) {
        Random random = new Random(2);
        for (int i = 0; i < count; i++) {
            long key = random.nextInt(1 << 20) * MAGIC;
            if (random.nextInt(10) == 0) {
                cache.put(key, new byte[random.nextInt(8192)]);
            } else {
                cache.get(key);
            }
        }
    }

    public static void testAll(ICache cache) {
        // Warm-up
        testWrite(cache, WARMUP_COUNT);
        testRead(cache, WARMUP_COUNT);
        testRead9Write1(cache, WARMUP_COUNT);

        String cacheClass = cache.getClass().getSimpleName();
        long start, end;

        start = System.currentTimeMillis();
        testWrite(cache, RUN_COUNT);
        end = System.currentTimeMillis();
        log(cacheClass + " write: " + (end - start));

        start = System.currentTimeMillis();
        testRead(cache, RUN_COUNT);
        end = System.currentTimeMillis();
        log(cacheClass + " read: " + (end - start));

        start = System.currentTimeMillis();
        testRead9Write1(cache, RUN_COUNT);
        end = System.currentTimeMillis();
        log(cacheClass + " read-write: " + (end - start));
    }

    private static void log(String str) {
        System.out.println(str);
    }

    public static void main(String[] args) throws Exception {
        String type = args.length == 0 ? null : args[0];
        ICache cache = null;
        if ("ehcache".equals(type)) {
            cache = new Ehcache(2*G);
        } else if ("jcs".equals(type)) {
            cache = new JCSCache("default");
        } else if ("chm".equals(type)) {
            cache = new ConcurrentHashMapCache(3000000, 256);
        } else if ("umc".equals(type)) {
            cache = new UnsafeMemoryCache(new MemoryCacheConfiguration(2*G, 200*K, "/dev/shm/cache-test"));
        } else {
            log("Nothing to run");
        }
        if(cache!=null) {
            testAll(cache);
            cache.close();
        }
    }
}
