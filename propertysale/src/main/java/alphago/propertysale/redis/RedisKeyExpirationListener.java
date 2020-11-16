package alphago.propertysale.redis;

import alphago.propertysale.entity.POJO.Auction;
import alphago.propertysale.service.AuctionService;
import alphago.propertysale.utils.RedisUtil;
import alphago.propertysale.utils.TimeUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @program: propertysale
 * @description: The listener for Redis Key Exploration.
 *               If key in redis has expired, the Listener will catch and handle it based on the Key.
 **/
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Autowired
    AuctionService auctionService;

    public static long getAuctionId(String key){
        return Long.parseLong(key.substring(key.indexOf(":") + 1));
    }

    /**
    * @Description: Handle the expired key.
     *              If the key in the form of "Start:aid", the system starts the auction aid.
     *              If the key in the form of "End:aid", the system find the auction aid.
    */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString();
        if(key.startsWith("Start:")){
            long aid = getAuctionId(key);
            Auction auction = auctionService.getById(aid);
            RedisTemplate redis = RedisUtil.getRedis();
            auctionService.initHistory(aid);
            System.out.println(auction.getEnddate());
            long expr = auction.getEnddate().toInstant(TimeUtil.getMyZone()).toEpochMilli() - System.currentTimeMillis();
            redis.opsForValue().set("End:"+aid,"", expr, TimeUnit.MILLISECONDS);
        }else if(key.startsWith("End:")){
            long aid = getAuctionId(key);
            // update status
            auctionService.finishAuction(aid);
        }
    }
}