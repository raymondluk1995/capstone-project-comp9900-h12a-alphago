package alphago.propertysale.service.impl;

import alphago.propertysale.entity.Auction;
import alphago.propertysale.entity.Rab;
import alphago.propertysale.entity.RabAction;
import alphago.propertysale.mapper.AuctionMapper;
import alphago.propertysale.mapper.RabActionMapper;
import alphago.propertysale.mapper.RabMapper;
import alphago.propertysale.service.RabActionService;
import alphago.propertysale.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-10-24
 */
@Service
@Transactional
public class RabActionServiceImpl extends ServiceImpl<RabActionMapper, RabAction> implements RabActionService {

    @Autowired
    private AuctionMapper auctionMapper;

    @Autowired
    private RabActionMapper rabActionMapper;

    @Autowired
    private RabMapper rabMapper;

    @Override
    public boolean bid(RabAction rabAction) {
        Long aid = rabAction.getActionId();
        Auction auction = auctionMapper.selectById(aid);
        // Check auction status
        if(!auction.getStatus().equals("A")){
            throw new RuntimeException("Auction: " + aid + " is end!");
        }
        // Check highest price
        long bidId = auction.getCurrentBid();
        RabAction bid = rabActionMapper.selectById(bidId);
        if(bid.getBidPrice().compareTo(rabAction.getBidPrice()) > 0){
            throw new RuntimeException("The bid price is smaller than current highest price!");
        }
        // add bid
        rabActionMapper.insert(rabAction);
        // update rab's highest price
        rabMapper.update(null, new UpdateWrapper<Rab>()
                                            .eq("rab_id", rabAction.getRabId())
                                            .set("highest_price", rabAction.getBidPrice()));
        // update auction's current bid
        auctionMapper.update(null, new UpdateWrapper<Auction>().eq("aid", aid).set("current_bid", rabAction.getActionId()));

        RedisTemplate redis = RedisUtil.valueRedis();
        String key = "End:"+aid;
        long expr = redis.getExpire(key);
        if(expr < 5 * 60 * 1000){
            redis.expire(key, expr + 2 * 60 * 1000 , TimeUnit.MILLISECONDS);
            return true;
        }else{
            return false;
        }
    }
}
