package alphago.propertysale.shiro.cahche;

import alphago.propertysale.utils.ApplicationContextUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collection;
import java.util.Set;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-09-27 15:45
 **/
public class RedisCache<K,V> implements Cache<K , V> {
    private String cacheName;

    public RedisCache(){}

    public RedisCache(String cacheName) {
        this.cacheName = cacheName;
    }

    public RedisTemplate getRedisTemplate(){
        RedisTemplate<K,V> redis = (RedisTemplate) ApplicationContextUtil.getApplicationContext().getBean("redisTemplate");
        redis.setKeySerializer(new StringRedisSerializer()); // 设置序列化方式
        redis.setHashKeySerializer(new StringRedisSerializer());
        return redis;
    }

    @Override
    public V get(K k) throws CacheException {
        RedisTemplate redisTemplate = getRedisTemplate();
        return (V)redisTemplate.opsForHash().get(cacheName , k.toString());
    }

    @Override
    public V put(K k, V v) throws CacheException {
        RedisTemplate redisTemplate = getRedisTemplate();
        redisTemplate.opsForHash().put(cacheName , k.toString() , v);
        return null;
    }

    @Override
    public V remove(K k) throws CacheException {
        return (V) getRedisTemplate().opsForHash().delete(cacheName , k.toString());
    }

    @Override
    public void clear() throws CacheException {
        getRedisTemplate().delete(cacheName);
    }

    @Override
    public int size() {
        return getRedisTemplate().opsForHash().size(cacheName).intValue();
    }

    @Override
    public Set<K> keys() {
        return getRedisTemplate().opsForHash().keys(cacheName);
    }

    @Override
    public Collection<V> values() {
        return getRedisTemplate().opsForHash().values(cacheName);
    }
}