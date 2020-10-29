package alphago.propertysale.service.impl;

import alphago.propertysale.entity.Auction;
import alphago.propertysale.entity.Rab;
import alphago.propertysale.entity.RabAction;
import alphago.propertysale.mapper.AuctionMapper;
import alphago.propertysale.mapper.RabActionMapper;
import alphago.propertysale.mapper.RabMapper;
import alphago.propertysale.service.RabService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-10-20
 */
@Service
@Transactional
public class RabServiceImpl extends ServiceImpl<RabMapper, Rab> implements RabService {

    @Autowired
    RabMapper rabMapper;
    @Autowired
    RabActionMapper rabActionMapper;
    @Autowired
    AuctionMapper auctionMapper;

    /**
     *  If auction is on going,
     */
    @Override
    public void rabRegister(Rab rab) {
        long aid = rab.getAid();
        Auction auction = auctionMapper.selectById(aid);
        if(auction.getStatus().equals("A")){
            long bidId = auction.getCurrentBid();
            String price = rabActionMapper.selectById(bidId).getBidPrice();
            if(price.compareTo(rab.getInitPrice())>0){
                rabMapper.insert(rab);
                RabAction bid = new RabAction();
                bid.setBidTime(rab.getRegisterTime()).setBidPrice(rab.getInitPrice()).setRabId(rab.getRabId());
                rabActionMapper.insert(bid);
            }else{
                throw new RuntimeException("Your bid price must larger than " + price + "!");
            }
        }else if(!auction.getStatus().equals("R")){
            throw new RuntimeException("Auction: " + aid + " is finished!");
        }else {
            rabMapper.insert(rab);
        }
    }

    @Override
    public boolean isRegistered(long uid, long aid) {
        return rabMapper.selectOne(new QueryWrapper<Rab>().eq("uid", uid).eq("aid", aid)) != null;
    }

    @Override
    public List<Rab> getRunningAuctions(long uid) {
        return rabMapper.getRunningAuctions(uid);
    }

    @Override
    public List<Rab> getPastAuctions(long uid) {
        return rabMapper.getPastAuctions(uid);
    }
}
