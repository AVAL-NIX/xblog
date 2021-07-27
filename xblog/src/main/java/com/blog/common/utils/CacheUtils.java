package com.blog.common.utils;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Component;

/**
 * 缓存配置类
 *
 * @author zx
 * @date 2019/2/10
 */
@Slf4j
@Component
public class CacheUtils {

    /**
     * 5min
     **/
    public static final String FIVE_MINUTES = "FiveMinutes";
    /**
     * 30min
     **/
    public static final String THIRTY_MINUTES = "ThirtyMinutes";
    /**
     * 2h
     **/
    public static final String TWO_HOURS = "TwoHours";
    /**
     * 24h
     **/
    public static final String TWENTYFOUR_HOURS = "TwentyFourHours";

    @Autowired
    public EhCacheCacheManager ehCacheCacheManager;

    private Cache getCache(String cacheName) {
        CacheManager cacheManager = ehCacheCacheManager.getCacheManager();
        if (cacheManager == null) {
            return null;
        }

        return cacheManager.getCache(cacheName);
    }

    /**
     * 设置缓存
     *
     * @param key   key
     * @param value value
     */
    public void set(String cacheName, String key, Object value) {
        Cache cache = getCache(cacheName);
        if (cache == null) {
            return;
        }
        cache.put(new Element(key, value));
    }

    /**
     * 获取缓存
     *
     * @param key key
     * @return value
     */
    public Object get(String cacheName, String key) {
        Cache cache = getCache(cacheName);
        if (cache == null) {
            return null;
        }

        return cache.get(key) != null ? cache.get(key).getObjectValue() : null;
    }

    /**
     * 移除缓存
     *
     * @param key key
     * @return value
     */
    public Object remove(String cacheName, String key) {
        Cache cache = getCache(cacheName);
        if (cache == null) {
            return null;
        }
        return cache.remove(key);
    }

}
