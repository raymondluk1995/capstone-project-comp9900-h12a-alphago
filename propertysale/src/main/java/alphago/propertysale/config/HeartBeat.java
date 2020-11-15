package alphago.propertysale.config;

import alphago.propertysale.utils.RedisUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @program: propertysale
 * @description: HeartBeat for redis to handle disconnection problem
 **/
@Component
public class HeartBeat {
    @Scheduled(cron = "0/30 * * * * *")
    public void heartBeat(){
        RedisUtil.valueRedis().opsForValue().get("heartbeat");
    }
}