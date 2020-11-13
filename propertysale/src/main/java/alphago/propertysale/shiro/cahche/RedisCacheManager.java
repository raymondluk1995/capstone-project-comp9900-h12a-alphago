package alphago.propertysale.shiro.cahche;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * @program: propertysale
 * @description: Redis Cache Manager for shiro
 *                  get corresponding cache based on the cache name;
 * @author: XIAO HAN
 * @create: 2020-09-27 15:43
 **/
public class RedisCacheManager implements CacheManager {
    @Override
    public <K, V> Cache<K, V> getCache(String cacheName) throws CacheException {
        return new RedisCache<>(cacheName);
    }
}