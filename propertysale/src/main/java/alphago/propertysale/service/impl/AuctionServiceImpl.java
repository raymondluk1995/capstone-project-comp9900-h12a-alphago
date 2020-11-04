package alphago.propertysale.service.impl;

import alphago.propertysale.entity.*;
import alphago.propertysale.entity.notification.FinishMessage;
import alphago.propertysale.entity.notification.Notification;
import alphago.propertysale.entity.returnVO.AuctionVO;
import alphago.propertysale.exception.AuctionNotFoundException;
import alphago.propertysale.mapper.*;
import alphago.propertysale.service.AuctionService;
import alphago.propertysale.utils.FileUtil;
import alphago.propertysale.utils.RedisUtil;
import alphago.propertysale.utils.TimeUtil;
import alphago.propertysale.websocket.BidHistoryPush;
import alphago.propertysale.websocket.BidMsg;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-16 16:49
 **/
@Service
@Transactional
public class AuctionServiceImpl extends ServiceImpl<AuctionMapper , Auction> implements AuctionService {
    @Autowired
    AuctionMapper auctionMapper;
    @Autowired
    PropertyMapper propertyMapper;
    @Autowired
    AddressMapper addressMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RabMapper rabMapper;
    @Autowired
    RabActionMapper rabActionMapper;
    @Autowired
    NotificationMapper notificationMapper;


    @Override
    public AuctionVO getAuctionByAid(long aid) {
        Auction auction = auctionMapper.selectById(aid);
        if(auction == null) {
            throw new AuctionNotFoundException("Auction " + aid + "is Not Found");
        }
        Property property = propertyMapper.selectById(auction.getPid());
        Address address = addressMapper.selectById(property.getPid());
        User user = userMapper.selectById(auction.getSeller());
        AuctionVO auctionVO = new AuctionVO();
        BeanUtils.copyProperties(auction , auctionVO);
        BeanUtils.copyProperties(property , auctionVO);
        BeanUtils.copyProperties(user , auctionVO);
        BeanUtils.copyProperties(address , auctionVO , "address");
        auctionVO.setAvatar(FileUtil.getUserAvatar(user));
        auctionVO.setPhotos(FileUtil.getImages(auction.getPid()));
        auctionVO.setAddress(address.getFullAddress());
        // get current bid price
        if(auction.getCurrentBid() != 0){
            long bid = auction.getCurrentBid();
            // get highest price
            Rab rab = rabMapper.selectById(auction.getCurrentBid());
            auctionVO.setLatestPrice(rab.getHighestPrice());
        }else{
            auctionVO.setLatestPrice(auction.getMinimumPrice());
        }
        auctionVO.setStartdate(auction.getStartdate().toInstant(TimeUtil.getMyZone()).toEpochMilli());
        auctionVO.setEnddate(auction.getEnddate().toInstant(TimeUtil.getMyZone()).toEpochMilli());
        return auctionVO;
    }

    @Deprecated
    @Override
    public AuctionVO getAuctionByProperty(long pid) {
        Auction auction = auctionMapper.selectOne(new QueryWrapper<Auction>()
                .eq("pid" , pid).eq("status" , "R"));
        return getAuctionByAid(auction.getAid());
    }

    @Override
    public AuctionStatus getAuctionStatus(long pid) {
        Auction auction = auctionMapper.getRunningAuctionById(pid);
        return new AuctionStatus().setAid(auction.getAid()).setStatus(auction.getStatus());
    }

    @Override
    public void auctionCancel(long pid, long aid) {
        // delete all Rab
        rabMapper.delete(new QueryWrapper<Rab>().eq("aid", aid));
        // delete auction
        auctionMapper.deleteById(aid);
        // change property auction status
        propertyMapper.update(new Property().setAuction(false),
                new UpdateWrapper<Property>().eq("pid",pid)
                .set("auction", false));
        // delete start count down
        RedisUtil.getRedis().delete("Start:"+aid);
    }

    /**
    * @Description: Initialize auction bid history when auction start
    */
    @Override
    public void initHistory(long aid) {
        // get all rabs
        List<Rab> rabList = rabMapper.selectList(new QueryWrapper<Rab>().eq("aid", aid));
        // sort by highest price
        rabList.sort(Comparator.comparing(Rab::getInitPrice));
        // bid
        BidHistoryPush.initHistory(aid);
        rabList.forEach(
                rab -> {
                    RabAction bid = new RabAction().setRabId(rab.getRabId()).
                            setBidPrice(rab.getInitPrice())
                            .setBidTime(rab.getRegisterTime());

                    BidMsg msg = new BidMsg().setUid(rab.getUid())
                                .setUsername(userMapper.selectById(rab.getUid()).getUsername())
                                .setPrice(rab.getInitPrice())
                                .setTime(bid.getBidTime().toInstant(TimeUtil.getMyZone()).toEpochMilli())
                                .setOvertime(false);

                    BidHistoryPush.addBidHistory(aid, msg);

                    rabActionMapper.insert(bid);
                }
        );
    }

    @Override
    public void finishAuction(long aid) {
        Auction auction = auctionMapper.selectById(aid);
        Property property = propertyMapper.selectById(auction.getPid());
        Address address = addressMapper.selectById(property.getPid());
        User seller = userMapper.selectById(auction.getSeller());

        FinishMessage message = new FinishMessage().setSuccess(true).setSeller(true)
                .setAid(aid)
                .setPid(auction.getPid())
                .setAddress(address.getFullAddress())
                .setSellerName(seller.getUsername())
                .setSellerFullName(seller.getFullName())
                .setSellerPhone(seller.getPhone())
                .setSellerEmail(seller.getEmail())
                .setHistory(BidHistoryPush.removeAuctionHistory(aid));

        if(auction.getCurrentBid() != 0) {
            Rab rab = rabMapper.selectById(auction.getCurrentBid());
            message.setBidPrice(rab.getHighestPrice());
            // Auction Success
            if(rab.getHighestPrice().compareTo(auction.getPrice())>=0){
                auctionMapper.update(null, new UpdateWrapper<Auction>().eq("aid", aid).set("status", "S"));
                propertyMapper.update(null, new UpdateWrapper<Property>()
                        .eq("pid", auction.getPid())
                        .set("auction", false)
                        .set("owner", rab.getUid()));

                User bidder = userMapper.selectById(rab.getUid());

                message.setBidderName(bidder.getUsername())
                        .setBidderFullName(bidder.getFullName())
                        .setSellerPhone(bidder.getPhone())
                        .setSellerEmail(bidder.getEmail());

                Notification notification = new Notification().setNotiType(Notification.FINISH)
                        .setCreateTime(LocalDateTime.now()).setIsRead(false).setUid(seller.getUid())
                        .setMessage(ObjectUtil.serialize(message));
                notificationMapper.addMessage(notification);

                message.setSeller(false);
                notification.setUid(bidder.getUid()).setMessage(ObjectUtil.serialize(message));
                notificationMapper.addMessage(notification);

                return;
            }
        }else{
            message.setBidPrice("No bid!");
        }
        // Auction Fail
        auctionMapper.update(null, new UpdateWrapper<Auction>().eq("aid", aid).set("status", "F"));
        propertyMapper.update(null, new UpdateWrapper<Property>()
                .eq("pid", auction.getPid())
                .set("auction", false));

        message.setSuccess(false);
        Notification notification = new Notification().setNotiType(Notification.FINISH)
                .setCreateTime(LocalDateTime.now()).setIsRead(false).setUid(seller.getUid())
                .setMessage(ObjectUtil.serialize(message));
        notificationMapper.addMessage(notification);
    }
}