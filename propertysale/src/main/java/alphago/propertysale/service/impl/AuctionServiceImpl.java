package alphago.propertysale.service.impl;

import alphago.propertysale.entity.POJO.*;
import alphago.propertysale.entity.inVO.SearchModel;
import alphago.propertysale.entity.notification.FinishMessage;
import alphago.propertysale.entity.notification.Notification;
import alphago.propertysale.entity.returnVO.AuctionVO;
import alphago.propertysale.entity.returnVO.RunningAuctionAddress;
import alphago.propertysale.entity.returnVO.SearchResVO;
import alphago.propertysale.entity.returnVO.SearchVO;
import alphago.propertysale.exception.AuctionFinishedException;
import alphago.propertysale.exception.AuctionNotFoundException;
import alphago.propertysale.mapper.*;
import alphago.propertysale.service.AuctionService;
import alphago.propertysale.shiro.JwtInfo;
import alphago.propertysale.utils.FileUtil;
import alphago.propertysale.utils.TimeUtil;
import alphago.propertysale.websocket.BidHistoryPush;
import alphago.propertysale.websocket.BidMsg;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @program: propertysale
 * @description: Implementation of Auction service
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

    @Autowired
    HistoryMapper historyMapper;

    private static final int TOP = 3;


    /**
    * @Description: Get Auction information according to Auction id
    */
    @Override
    public AuctionVO getAuctionByAid(long aid) {
        Auction auction = auctionMapper.selectById(aid);
        if (auction == null) {
            throw new AuctionNotFoundException("Auction " + aid + " is Not Found");
        }
        if (auction.isFinish()) {
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
        // Update History
        Subject subject = SecurityUtils.getSubject();

        RunningAuctionAddress rec = new RunningAuctionAddress();
        if (subject.isAuthenticated()) {
            JwtInfo info = (JwtInfo) subject.getPrincipal();
            long uid = info.getUid();
            HistoryUpdater updater = new HistoryUpdater().setUid(uid).setBathroomNum(property.getBathroomNum())
                    .setBedroomNum(property.getBedroomNum()).setGarageNum(property.getGarageNum())
                    .setLat(Double.parseDouble(address.getLat()))
                    .setLng(Double.parseDouble(address.getLng()));
            historyMapper.updateByVisit(updater);
        }
        rec.setBathroomNum((double) property.getBathroomNum()).setBedroomNum((double) property.getBedroomNum())
                .setGarageNum((double) property.getGarageNum())
                .setLat(Double.valueOf(address.getLat())).setLng(Double.valueOf(address.getLng()));
        rec.setAid(aid);

        List<RunningAuctionAddress> list = auctionMapper.getRunningAuctionAddress();
        auctionVO.setRecommendations(HistoryServiceImpl.recommendations(rec, list));
        return auctionVO;
    }

    /**
     * @Description: Get Auction information according to Property id
     */
    @Deprecated
    @Override
    public AuctionVO getAuctionByProperty(long pid) {
        Auction auction = auctionMapper.selectOne(new QueryWrapper<Auction>()
                .eq("pid", pid).eq("status", "R"));
        return getAuctionByAid(auction.getAid());
    }

    /**
     * @Description: If a property is register as an auction:
     *               'R': Auction not start
     *               'A': Auction has started
     *               'S': Auction finished, success
     *               'F': Auction finished, passed in
     *
     */
    @Override
    public AuctionStatus getAuctionStatus(long pid) {
        Auction auction = auctionMapper.getRunningAuctionById(pid);
        return new AuctionStatus().setAid(auction.getAid()).setStatus(auction.getStatus());
    }

    /**
    * @Description: Cancel an auction
    */
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
        // Check status
        Auction auction = auctionMapper.selectById(aid);
        if(!auction.getStatus().equals("R")) return;
        // get all rabs
        List<Rab> rabList = rabMapper.selectList(new QueryWrapper<Rab>().eq("aid", aid));
        // sort by highest price
        rabList.sort(Comparator.comparing(Rab::getInitPrice));
        // bid
        BidHistoryPush.initHistory(aid);
        Rab currentRab = null;
        for (Rab rab : rabList) {
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
        if (currentRab != null) {
            auctionMapper.update(null,
                    new UpdateWrapper<Auction>()
                            .eq("aid", aid)
                            .set("current_bid", currentRab.getRabId())
                            .set("highest_price", currentRab.getHighestPrice()));
        }
        BidHistoryPush.refresh(aid);
    }

    @Override
    public void finishAuction(long aid) {
        Auction auction = auctionMapper.selectById(aid);
        // Check status
        if(!auction.getStatus().equals("A")) return;

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

        if (auction.getCurrentBid() != 0) {
            Rab rab = rabMapper.selectById(auction.getCurrentBid());
            message.setBidPrice(rab.getHighestPrice());
            // Auction Success
            if (rab.getHighestPrice()>= auction.getPrice()) {
                auctionMapper.update(null, new UpdateWrapper<Auction>().eq("aid", aid).set("status", "S"));
                propertyMapper.update(null, new UpdateWrapper<Property>()
                        .eq("pid", auction.getPid())
                        .set("auction", false)
                        .set("owner", rab.getUid()));

                User bidder = userMapper.selectById(rab.getUid());

                message.setBidderName(bidder.getUsername())
                        .setBidderFullName(bidder.getFullName())
                        .setBidderPhone(bidder.getPhone())
                        .setBidderEmail(bidder.getEmail());

                Notification notification = new Notification().setNotiType(Notification.FINISH)
                        .setCreateTime(LocalDateTime.now()).setIsRead(false).setUid(seller.getUid())
                        .setMessage(ObjectUtil.serialize(message));
                notificationMapper.addMessage(notification);

                message.setSeller(false);
                notification.setUid(bidder.getUid()).setMessage(ObjectUtil.serialize(message));
                notificationMapper.addMessage(notification);

                return;
            }
        } else {
            message.setBidPrice(null);
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
        BidHistoryPush.refresh(aid);
    }
    /**
    * @Description: Search auctions based on the search model
    */
    @Override
    public SearchVO getSearchVO(SearchModel model) {
        Subject subject = SecurityUtils.getSubject();
        SearchVO vo = new SearchVO();
        Page<Auction> page = new Page<>(model.getCurrPage(), 6);
        IPage<Auction> res = null;
        boolean update = false;

        List<SearchResVO> ret = new ArrayList<>();
        vo.setResVOList(ret);
        if (model.isAllEmpty()) {
            // if you are just a ranger.
            if(model.getOrder() == null) {
                res = auctionMapper.selectPage(page, new QueryWrapper<Auction>().eq("status", 'A').or().eq("status", "R")
                        .orderByDesc("aid"));
            }else if(model.getOrder().equals("plusPrice")){
                res = auctionMapper.selectPage(page, new QueryWrapper<Auction>().eq("status", 'A').or().eq("status", "R")
                        .orderByAsc("highest_price"));
            }else{
                res = auctionMapper.selectPage(page, new QueryWrapper<Auction>().eq("status", 'A').or().eq("status", "R")
                        .orderByDesc("highest_price"));
            }
            List<Auction> runningOrComingAuctions = /* auctionMapper.getAllRunningOrComingAuction();*/ res.getRecords();
            for (Auction auction : runningOrComingAuctions) {
                SearchResVO searchResVO = new SearchResVO();
                searchResVO.setBidderNum(auction.getBidderNum());
                if (auction.getStatus().equals("R")) {
                    // registered bid.
                    // set to minimum price
                    searchResVO.setHighestPrice(auction.getMinimumPrice());
                } else if (auction.getStatus().equals("A")) {
                    // if you are coming bid.
                    long currBid = auction.getCurrentBid();
                    if (currBid == 0) {
                        // set to Not bid
                        searchResVO.setHighestPrice(auction.getMinimumPrice());
                    } else {
                        // set to rab.highest Price
                        Rab rab = rabMapper.selectById(auction.getCurrentBid());
                        searchResVO.setHighestPrice(rab.getHighestPrice());
                    }
                }
                Property property = propertyMapper.selectOne(new QueryWrapper<Property>().eq("pid", auction.getPid()));
                BeanUtils.copyProperties(property, searchResVO);
                searchResVO.setAid(auction.getAid());
                searchResVO.setPhotos(FileUtil.getImages(property.getPid()));
                searchResVO.setStatus(auction.getStatus());
                searchResVO.setStartdate(auction.getStartdate());
                searchResVO.setEnddate(auction.getEnddate());
                Address address = addressMapper.selectById(property.getPid());
                searchResVO.setAddress(address.getFullAddress());
                vo.getResVOList().add(searchResVO);
            }

            vo.setCurrPage(model.getCurrPage());
            vo.setTotalProp(res.getTotal());
        } else {
            QueryWrapper<SearchResVO> voQueryWrapper = new QueryWrapper<>();
            UpdateWrapper<History> updateWrapper = new UpdateWrapper<>();
            if (model.getSuburb() != null) {
                voQueryWrapper.eq("a.suburb", model.getSuburb());
            }
            if (model.getState() != null) {
                voQueryWrapper.eq("a.state", model.getState());
            }
            if (model.getLat() != null){
                update = true;
                updateWrapper.setSql("lat = lat +" + model.getLat());
                updateWrapper.setSql("lat_cnt = lat_cnt + 1");
            }
            if (model.getLng() != null){
                update = true;
                updateWrapper.setSql("lng = lng +" + model.getLng());
                updateWrapper.setSql("lng_cnt = lng_cnt + 1");
            }
            if (model.getPostcode() != null) {
                voQueryWrapper.eq("a.postcode", model.getPostcode());
            }

            if (model.getStartDate() != null) {
                voQueryWrapper.ge("auc.start_date", model.getStartDate());
            }
            if (model.getEndDate() != null) {
                voQueryWrapper.le("auc.end_date", model.getEndDate());
            }
            if (model.getMinPrice() != null) {
                voQueryWrapper.ge("auc.minimum_price", model.getMinPrice());
            }
            if (model.getMaxPrice() != null) {
                voQueryWrapper.le("auc.highest_price", model.getMaxPrice());
            }

            if (model.getBedrooms() != null) {
                if(model.getBedrooms() == 5){
                    voQueryWrapper.ge("prop.bedroom_num", model.getBedrooms());
                }else {
                    voQueryWrapper.eq("prop.bedroom_num", model.getBedrooms());
                }
                update = true;
                updateWrapper.setSql("bedroom_num = bedroom_num +" + model.getBedrooms());
                updateWrapper.setSql("bedroom_cnt = bedroom_cnt + 1");
            }
            if (model.getBathrooms() != null) {
                if(model.getBathrooms() == 5){
                    voQueryWrapper.ge("prop.bathroom_num", model.getBathrooms());
                }else {
                    voQueryWrapper.eq("prop.bathroom_num", model.getBathrooms());
                }
                update = true;
                updateWrapper.setSql("bathroom_num = bathroom_num +" + model.getBathrooms());
                updateWrapper.setSql("bathroom_cnt = bathroom_cnt + 1");
            }
            if (model.getGarages() != null) {
                if(model.getGarages() == 5){
                    voQueryWrapper.ge("prop.garage_num", model.getGarages());
                }else {
                    voQueryWrapper.eq("prop.garage_num", model.getGarages());
                }
                update = true;
                updateWrapper.setSql("garage_num = garage_num +" + model.getGarages());
                updateWrapper.setSql("garage_cnt = garage_cnt + 1");
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
            Page<SearchResVO> searchResVOPage = new Page<SearchResVO>(model.getCurrPage(), 6);

            IPage<SearchResVO> searchResVOS = null;

            if(model.getOrder() == null){
                searchResVOS = auctionMapper.getAllRunningOrComingRes(searchResVOPage, voQueryWrapper);
            } else if (model.getOrder().equals("plusPrice")) {
                // asc
                searchResVOS = auctionMapper.getAllRunningOrComingResASC(searchResVOPage, voQueryWrapper);
            }else{
                searchResVOS = auctionMapper.getAllRunningOrComingResDESC(searchResVOPage, voQueryWrapper);
            }
            // get their address.
            List<SearchResVO> resVOS = searchResVOS.getRecords();
            for (SearchResVO searchResVO : resVOS) {
                // set their full address.
                Address address = addressMapper.selectById(searchResVO.getPid());
                searchResVO.setAddress(address.getFullAddress());
                // photos
                searchResVO.setPhotos(FileUtil.getImages(searchResVO.getPid()));
            }

            // update history
            if(update && subject.isAuthenticated()){
                long uid = ((JwtInfo)subject.getPrincipal()).getUid();
                historyMapper.update(null, updateWrapper.eq("uid", uid));
            }
            vo.setResVOList(searchResVOS.getRecords());
            vo.setCurrPage(model.getCurrPage());
            vo.setTotalProp(searchResVOS.getTotal());
        }
        // get recommendations
        if(subject.isAuthenticated()) {
            long uid = ((JwtInfo) subject.getPrincipal()).getUid();
            RunningAuctionAddress query = new RunningAuctionAddress();
            History history = historyMapper.selectById(uid);

            if (history.getBathroomCnt() == 0) query.setBathroomNum(1D);
            else query.setBathroomNum(history.getBathroomNum() / history.getBathroomCnt());
            if (history.getBedroomCnt() == 0) query.setBedroomNum(2D);
            else query.setBedroomNum(history.getBedroomNum() / history.getBedroomCnt());
            if (history.getGarageCnt() == 0) query.setGarageNum(1D);
            else query.setGarageNum(history.getGarageNum() / history.getGarageCnt());
            if (history.getLatCnt() == 0) query.setLat(-33.870893);
            else query.setLat(history.getLat() / history.getLatCnt());
            if (history.getLngCnt() == 0) query.setLng(151.209303);
            else query.setLng(history.getLng() / history.getLngCnt());


            List<RunningAuctionAddress> list = auctionMapper.getRunningAuctionAddress();
            vo.setRecommendations(HistoryServiceImpl.recommendations(query, list));
        }
        return vo;
    }
}