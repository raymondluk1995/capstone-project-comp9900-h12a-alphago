package alphago.propertysale.service.impl;

import alphago.propertysale.entity.*;
import alphago.propertysale.entity.inVO.SearchModel;
import alphago.propertysale.entity.notification.FinishMessage;
import alphago.propertysale.entity.notification.Notification;
import alphago.propertysale.entity.returnVO.AuctionVO;
import alphago.propertysale.exception.AuctionFinishedException;
import alphago.propertysale.exception.AuctionNotFoundException;
import alphago.propertysale.entity.returnVO.SearchResVO;
import alphago.propertysale.entity.returnVO.SearchVO;
import alphago.propertysale.exception.AuctionNotFoundException;
import alphago.propertysale.mapper.*;
import alphago.propertysale.service.AuctionService;
import alphago.propertysale.utils.FileUtil;
import alphago.propertysale.utils.PriceUtil;
import alphago.propertysale.utils.RedisUtil;
import alphago.propertysale.utils.TimeUtil;
import alphago.propertysale.websocket.BidHistoryPush;
import alphago.propertysale.websocket.BidMsg;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN, TAO XUE
 * @create: 2020-10-16 16:49
 **/
@Service
@Transactional
public class AuctionServiceImpl extends ServiceImpl<AuctionMapper, Auction> implements AuctionService {
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
            throw new AuctionNotFoundException("Auction " + aid + " is Not Found");
        }
        if(auction.isFinish()){
            throw new AuctionFinishedException("Auction " + aid + " is Finished");
        }
        Property property = propertyMapper.selectById(auction.getPid());
        Address address = addressMapper.selectById(property.getPid());
        User user = userMapper.selectById(auction.getSeller());
        AuctionVO auctionVO = new AuctionVO();
        BeanUtils.copyProperties(auction, auctionVO);
        BeanUtils.copyProperties(property, auctionVO);
        BeanUtils.copyProperties(user, auctionVO);
        BeanUtils.copyProperties(address, auctionVO, "address");
        auctionVO.setAvatar(FileUtil.getUserAvatar(user));
        auctionVO.setPhotos(FileUtil.getImages(auction.getPid()));
        auctionVO.setAddress(address.getFullAddress());
        // get current bid price
        if (auction.getCurrentBid() != 0) {
            long bid = auction.getCurrentBid();
            // get highest price
            Rab rab = rabMapper.selectById(auction.getCurrentBid());
            auctionVO.setLatestPrice(rab.getHighestPrice());
        } else {
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
                .eq("pid", pid).eq("status", "R"));
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
        propertyMapper.update(new Property().setAuction(false), new UpdateWrapper<Property>().eq("pid", pid));
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
        Rab currentRab = null;
        for(Rab rab : rabList){
            currentRab = rab;

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
        // set current rab
        if(currentRab != null) {
            auctionMapper.update(null, new UpdateWrapper<Auction>().set("current_bid", currentRab.getUid()));
        }
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
            if(PriceUtil.priceCompare(rab.getHighestPrice(), auction.getPrice()) >=0){
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
            message.setBidPrice("No Bid!");
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

    @Override
    public SearchVO getSearchVO(SearchModel model) {
        SearchVO vo = new SearchVO();
        Page<Auction> page = new Page<>(model.getCurrPage(), 6);
        IPage<Auction> res = null;
        res = auctionMapper.selectPage(page, new QueryWrapper<Auction>().eq("status", 'A').or().eq("status", "R"));
        List<Auction> runningOrComingAuctions = /* auctionMapper.getAllRunningOrComingAuction();*/ res.getRecords();

        List<SearchResVO> ret = new ArrayList<>();
        vo.setResVOList(ret);
        if (model.isAllEmpty()) {
            // if you are just a ranger.
            for (Auction auction : runningOrComingAuctions) {
                SearchResVO searchResVO = new SearchResVO();
                searchResVO.setBidderNum(auction.getBidderNum());
                if (auction.getStatus().equals("R")) {
                    // registered bid.
                    // set to minimum price
                    searchResVO.setCurrentBid(auction.getMinimumPrice());
                } else if (auction.getStatus().equals("A")) {
                    // if you are coming bid.
                    long currBid = auction.getCurrentBid();
                    if (currBid == 0) {
                        // set to Not bid

                        searchResVO.setCurrentBid(auction.getMinimumPrice());
                    } else {
                        // set to rab.highest Price
                        Rab rab = rabMapper.selectById(auction.getCurrentBid());
                        searchResVO.setCurrentBid(rab.getHighestPrice());
                    }
                }
                Property property = propertyMapper.selectOne(new QueryWrapper<Property>().eq("pid", auction.getPid()));
                BeanUtils.copyProperties(property, searchResVO);
                searchResVO.setPhotos(FileUtil.getImages(property.getPid()));
                searchResVO.setStatus(auction.getStatus());
                Address address = addressMapper.selectById(property.getPid());
                searchResVO.setAddress(address.getFullAddress());
                vo.getResVOList().add(searchResVO);
            }

            vo.setCurrPage(model.getCurrPage());
            vo.setTotalPage(res.getTotal());
        } else {
            QueryWrapper<SearchResVO> voQueryWrapper = new QueryWrapper<>();
            if (model.getSuburb() != null) {
                voQueryWrapper.eq("a.suburb", model.getSuburb());
            }
            if (model.getPostcode() != null) {
                voQueryWrapper.eq("a.postcode", model.getPostcode());
            }

            if (model.getStartDate() != null) {
                voQueryWrapper.eq("auc.start_date", model.getStartDate());
            }
            if (model.getEndDate() != null) {
                voQueryWrapper.eq("auc.end_date", model.getEndDate());
            }
            if (model.getMinPrice() != null) {
                voQueryWrapper.eq("auc.minimum_price", model.getMinPrice());
            }
            if (model.getMaxPrice() != null) {
                voQueryWrapper.eq("rab.highest_price", model.getMaxPrice());
            }

            if (model.getBedRooms() != null) {
                voQueryWrapper.eq("prop.bedroom_num", model.getBedRooms());
            }
            if (model.getBathRooms() != null) {
                voQueryWrapper.eq("prop.bathroom_num", model.getBathRooms());
            }
            if (model.getGarages() != null) {
                voQueryWrapper.eq("prop.garage_num", model.getGarages());
            }
            if (model.getPropertyType() != null) {
                voQueryWrapper.eq("prop.type", model.getPropertyType());
            }

            if (model.getMinArea() != null) {
                voQueryWrapper.ge("prop.area", model.getMinArea());
            }
            if (model.getMaxArea() != null) {
                voQueryWrapper.le("prop.area", model.getMaxArea());
            }
            if (model.getOrder() != null) {
                if (model.getOrder().equals("price")) {
                    // asc
                    voQueryWrapper.orderByAsc("");
                } else if (model.getOrder().equals("-price")) {
                    voQueryWrapper.orderByDesc("");
                }
            }
            Page<SearchResVO> searchResVOPage = new Page<SearchResVO>(model.getCurrPage(), 6);

            IPage<SearchResVO> searchResVOS = auctionMapper.getAllRunningOrComingRes(searchResVOPage, voQueryWrapper);

            // get their address.
            List<SearchResVO> resVOS = searchResVOS.getRecords();
            for (SearchResVO searchResVO: resVOS) {
                // set their full address.
                Address address = addressMapper.selectById(searchResVO.getPid());
                searchResVO.setAddress(address.getFullAddress());
                // photos
                searchResVO.setPhotos(FileUtil.getImages(searchResVO.getPid()));
            }

            vo.setResVOList(searchResVOS.getRecords());
            vo.setCurrPage(model.getCurrPage());
            vo.setTotalPage(searchResVOS.getTotal());
        }
        return vo;
    }
}