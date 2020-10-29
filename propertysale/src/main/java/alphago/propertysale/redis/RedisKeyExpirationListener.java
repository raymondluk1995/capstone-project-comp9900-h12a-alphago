package alphago.propertysale.redis;

import alphago.propertysale.entity.Auction;
import alphago.propertysale.service.AuctionService;
import alphago.propertysale.utils.ApplicationContextUtil;
import alphago.propertysale.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-20 16:58
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

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString();
        if(key.startsWith("Start:")){
            long aid = getAuctionId(key);
            Auction auction = new Auction().setAid(aid).setStatus("A");
            auctionService.update(new UpdateWrapper<Auction>().eq("aid" , aid).set("status" , Auction.AUCTION));
            RedisTemplate redis = RedisUtil.getRedis();

            auctionService.initHistory(aid);

            long expr = auction.getEnddate().toInstant(ZoneOffset.UTC).toEpochMilli() - System.currentTimeMillis();
            redis.opsForValue().set("End:"+aid,"", expr, TimeUnit.MILLISECONDS);
        }else if(key.startsWith("End:")){
            long aid = getAuctionId(key);
            // update status
            auctionService.finishAuction(aid);
            // send message
        }
    }
}