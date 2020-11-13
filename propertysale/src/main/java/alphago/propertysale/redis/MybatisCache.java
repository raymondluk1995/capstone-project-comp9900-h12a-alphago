package alphago.propertysale.redis;

import alphago.propertysale.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @program: propertysale
 * @description: RedisCache for Mybaits
 * @author: XIAO HAN
 * @create: 2020-10-11 14:39
 **/
@Slf4j
public class MybatisCache implements Cache {
    private String id;
    private static final long TIMEOUT = 30;
    private static final TimeUnit UNIT = TimeUnit.MINUTES;

    // Init Redis Cache
    public MybatisCache(String id) {
        if(id == null) throw new IllegalArgumentException("Need Cache Id");
        this.id = id;
        log.info("Set Cache Id " + id);
    }

    @Override
    public String getId() {
        log.info("Get Cache Id: " + id);
        return id;
    }

    @Override
    public void putObject(Object k, Object v) {
        RedisTemplate redisTemplate = RedisUtil.getHashRedis();
        redisTemplate.opsForHash().put(id , k , v);
        log.info("Add Cache: " + k.toString());
    }

    @Override
    public Object getObject(Object k) {
        RedisTemplate redisTemplate = RedisUtil.getHashRedis();
        Object val = redisTemplate.opsForHash().get(id , k);
        log.info("Get Cache: key -->" + k + "value -->" + val);
        return val;
    }

    @Override
    public Object removeObject(Object key) {
        RedisTemplate redisTemplate = RedisUtil.getHashRedis();
        redisTemplate.opsForHash().delete(id , key);
        log.debug("Remove cached query result from redis");
        return null;
    }

    @Override
    public void clear() {
        RedisTemplate redisTemplate = RedisUtil.getHashRedis();
        redisTemplate.delete(id);
    }

    @Override
    public int getSize() {
        return 0;
    }
}