package alphago.propertysale.controller;

import alphago.propertysale.entity.Address;
import alphago.propertysale.entity.Auction;
import alphago.propertysale.entity.Property;
import alphago.propertysale.entity.Rab;
import alphago.propertysale.entity.returnVO.AuctionVO;
import alphago.propertysale.entity.returnVO.RabVO;
import alphago.propertysale.service.*;
import alphago.propertysale.shiro.JwtInfo;
import alphago.propertysale.utils.FileUtil;
import alphago.propertysale.utils.Result;
import alphago.propertysale.utils.TimeUtil;
import alphago.propertysale.websocket.BidHistoryPush;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-19 20:06
 **/
@RestController
@RequestMapping("/auction")
public class AuctionController {
    @Autowired
    AuctionService auctionService;
    @Autowired
    PropertyService propertyService;
    @Autowired
    AddressService addressService;
    @Autowired
    RabService rabService;
    @Autowired
    RabActionService actionService;

    @Autowired
    HistoryService historyService;

    // get rab's auction information
    @RequiresAuthentication
    @RequestMapping("/list/now")
    public Result auctionListNow(){
        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo) subject.getPrincipal();
        long uid = info.getUid();

        List<Rab> list = rabService.getRunningAuctions(uid);

        return Result.success(getAuctionList(list));
    }

    @RequiresAuthentication
    @RequestMapping("/list/past")
    public Result auctionListPast() {
        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo) subject.getPrincipal();
        long uid = info.getUid();

        List<Rab> list = rabService.getPastAuctions(uid);

        return Result.success(getAuctionList(list));
    }

    // Get auction information based on Auction Id
    @RequestMapping("/information/{aid}")
    public Result auctionInformation(@PathVariable long aid){
        AuctionVO auctionVO = auctionService.getAuctionByAid(aid);
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()) {
            JwtInfo info = (JwtInfo) subject.getPrincipal();
            long uid = info.getUid();

            Rab rab = rabService.getOne(new QueryWrapper<Rab>()
                    .eq("uid", uid).eq("aid", aid));
            if(rab != null) {
                auctionVO.setRab(String.valueOf(rab.getRabId()));
                auctionVO.setHighestPrice(rab.getHighestPrice());
            }else{
                auctionVO.setRab(null);
            }
        }
        auctionVO.setHistory(BidHistoryPush.getAuctionHistory(aid));
        return Result.success(auctionVO);
    }

    private List<RabVO> getAuctionList(List<Rab> list) {
        List<RabVO> ret = new ArrayList<>();

        for(Rab rab : list){
            Auction auction = auctionService.getById(rab.getAid());
            Property property = propertyService.getById(auction.getPid());
            Address address = addressService.getById(property.getPid());
            List<String> photos = FileUtil.getImages(property.getPid());

            RabVO rabVO = new RabVO();
            BeanUtils.copyProperties(auction, rabVO, "startdate" , "enddate");
            rabVO.setStartdate(auction.getStartdate().toInstant(TimeUtil.getMyZone()).toEpochMilli());
            rabVO.setEnddate(auction.getEnddate().toInstant(TimeUtil.getMyZone()).toEpochMilli());
            BeanUtils.copyProperties(property, rabVO);
            rabVO.setAddress(address.getFullAddress());
            rabVO.setPhotos(photos);
            rabVO.setHighestPrice(rab.getHighestPrice());

            if(auction.getStatus().equals("A")||auction.getStatus().equals("S") || auction.getStatus().equals("F")){
                if(auction.getCurrentBid() == 0){
                    rabVO.setCurrentBid("NO BID");
                }else {
                    rabVO.setCurrentBid(rabService.getOne(new QueryWrapper<Rab>()
                            .eq("rab_id", auction.getCurrentBid()))
                            .getHighestPrice());
                }
            }else{
                rabVO.setCurrentBid(auction.getMinimumPrice());
            }
            ret.add(rabVO);
        }
        return ret;
    }
}