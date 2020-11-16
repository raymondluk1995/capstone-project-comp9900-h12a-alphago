package alphago.propertysale.service.impl;

import alphago.propertysale.entity.POJO.Auction;
import alphago.propertysale.entity.POJO.Rab;
import alphago.propertysale.entity.POJO.RabAction;
import alphago.propertysale.mapper.AuctionMapper;
import alphago.propertysale.mapper.RabActionMapper;
import alphago.propertysale.mapper.RabMapper;
import alphago.propertysale.mapper.UserMapper;
import alphago.propertysale.service.RabService;
import alphago.propertysale.utils.TimeUtil;
import alphago.propertysale.websocket.BidHistoryPush;
import alphago.propertysale.websocket.BidMsg;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description: Implementation of Rab service
 */
@Service
@Transactional
public class RabServiceImpl extends ServiceImpl<RabMapper, Rab> implements RabService {

    @Autowired
    private RabMapper rabMapper;
    @Autowired
    private RabActionMapper rabActionMapper;
    @Autowired
    private AuctionMapper auctionMapper;
    @Autowired
    private UserMapper userMapper;
    /**
     *  Check if auction is on going,
     */
    @Override
    public void rabRegister(Rab rab) {
        long aid = rab.getAid();
        long uid = rab.getUid();
        Auction auction = auctionMapper.selectById(aid);
        if(auction.getStatus().equals("A")){
            Long curPrice = auction.getHighestPrice();
            if(curPrice < rab.getInitPrice()){
                rabMapper.insert(rab);
                RabAction bid = new RabAction();
                bid.setBidTime(rab.getRegisterTime()).setBidPrice(rab.getInitPrice()).setRabId(rab.getRabId());
                rabActionMapper.insert(bid);
                auctionMapper.update(null, new UpdateWrapper<Auction>()
                        .eq("aid", aid)
                        .set("current_bid", rab.getRabId())
                        .set("highest_price", rab.getHighestPrice()));

                boolean overtime = Auction.isExpr(aid);

                if(overtime){
                    auctionMapper.update(null, new UpdateWrapper<Auction>()
                            .eq("aid", aid)
                            .setSql("end_date = date_add(end_date, INTERVAL 2 MINUTE)")
                    );
                }

                BidHistoryPush.bidPush(aid,
                        new BidMsg().setUid(uid)
                                .setTime(bid.getBidTime().toInstant(TimeUtil.getMyZone()).toEpochMilli())
                                .setUsername(userMapper.selectById(uid).getUsername())
                                .setPrice(rab.getHighestPrice())
                                .setOvertime(overtime)
                                .setNewBidder(true));
            }else{
                throw new RuntimeException("Your bid price must larger than " + curPrice + "!");
            }
        }else if(!auction.getStatus().equals("R")){
            throw new RuntimeException("Auction: " + aid + " has finished!");
        }else {
            rab.setHighestPrice(auction.getMinimumPrice()).setInitPrice(auction.getMinimumPrice());
            auctionMapper.update(null, new UpdateWrapper<Auction>().eq("aid", aid)
                    .setSql("minimum_price = minimum_price+10"));
            rabMapper.insert(rab);
            BidHistoryPush.newBidder(aid);
        }
        auctionMapper.update(null, new UpdateWrapper<Auction>().eq("aid", aid).setSql("bidder_num=bidder_num+1"));
    }

    /**
    * @Description: Check if user has registered the auction
    */
    @Override
    public boolean isRegistered(long uid, long aid) {
        return rabMapper.selectOne(new QueryWrapper<Rab>().eq("uid", uid).eq("aid", aid)) != null;
    }

    /**
    * @Description: Get all of users' auctions
    */
    @Override
    public List<Rab> getRunningAuctions(long uid) {
        List<Rab> auctions = rabMapper.getRunningAuctions(uid);
        auctions.forEach(
                auction -> {

                }
        );
        return auctions;
    }

    /**
    * @Description: Get all past auctions
    */
    @Override
    public List<Rab> getPastAuctions(long uid) {
        return rabMapper.getPastAuctions(uid);
    }
}
