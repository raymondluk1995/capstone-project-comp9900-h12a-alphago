package alphago.propertysale.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @program: propertysale
 **/
public class RedisUtil {

    /**
    * @Description: Return redisTemplate for Hash usage
    */
    public static RedisTemplate getHashRedis(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtil.getApplicationContext().getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }

    /**
     * @Description: Return redisTemplate for String usage
     */
    public static RedisTemplate valueRedis(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtil.getApplicationContext().getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    /**
     * @Description: Return Default redisTemplate
     */
    public static RedisTemplate getRedis(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtil.getApplicationContext().getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }
}