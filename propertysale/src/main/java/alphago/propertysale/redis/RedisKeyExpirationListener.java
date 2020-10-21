package alphago.propertysale.redis;

import alphago.propertysale.entity.Auction;
import alphago.propertysale.service.AuctionService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

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

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString();
        if(key.startsWith("Start:")){
            long aid = Long.parseLong(key.substring(key.indexOf(":") + 1));
            Auction auction = new Auction().setAid(aid).setStatus("A");
            auctionService.update(new UpdateWrapper<Auction>().eq("aid" , aid).set("status" , Auction.AUCTION));
        }
    }
}