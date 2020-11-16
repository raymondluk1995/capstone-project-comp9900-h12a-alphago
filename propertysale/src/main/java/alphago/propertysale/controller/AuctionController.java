package alphago.propertysale.controller;

import alphago.propertysale.entity.POJO.Address;
import alphago.propertysale.entity.POJO.Auction;
import alphago.propertysale.entity.POJO.Property;
import alphago.propertysale.entity.POJO.Rab;
import alphago.propertysale.entity.returnVO.AuctionVO;
import alphago.propertysale.entity.returnVO.RabVO;
import alphago.propertysale.service.*;
import alphago.propertysale.shiro.JwtInfo;
import alphago.propertysale.utils.FileUtil;
import alphago.propertysale.utils.Result;
import alphago.propertysale.utils.TimeUtil;
import alphago.propertysale.websocket.BidHistoryPush;
import alphago.propertysale.websocket.BidMsg;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @description: Controller to deal with auction's request
 *              Springboot will dispatch request to exact
 *              function based on the URL.
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
    /**
     * Get auction information based on Auction Id
     * If user is authenticated, also get the auction
     * Bidders' information.
     *
     * Update User's visit history
     *
     * Also Get similar properties based on the auction inforamtion
    */
    @RequestMapping("/information/{aid}")
    public Result auctionInformation(@PathVariable long aid){
        AuctionVO auctionVO = auctionService.getAuctionByAid(aid);
        Subject subject = SecurityUtils.getSubject();
        // If the user is authenticated
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
        // Get auction's bid history
        if(auctionVO.getStatus().equals("A")) {
            List<BidMsg> temp = BidHistoryPush.getAuctionHistory(aid);
            List<BidMsg> history = new ArrayList<>();
            for (Object obj : temp) {
                BidMsg bidMsg = new BidMsg();
                BeanUtils.copyProperties(obj, bidMsg);
                history.add(bidMsg);
            }
            history.sort(Comparator.comparing(BidMsg::getPrice));
            auctionVO.setHistory(history);
        }
        return Result.success(auctionVO);
    }

    /**
    * @Description: Get already started or not start auctions registered by the user
    * @Param:
    * @return: List of auction information
    */
    @RequiresAuthentication
    @RequestMapping("/list/now")
    public Result auctionListNow(){
        // Get user id
        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo) subject.getPrincipal();
        long uid = info.getUid();
        // Get auctions
        List<Rab> list = rabService.getRunningAuctions(uid);

        return Result.success(getAuctionList(list));
    }

    /**
     * @Description: Get finished auctions registered by the user
     * @Param:
     * @return: List of auction information
     */
    @RequiresAuthentication
    @RequestMapping("/list/past")
    public Result auctionListPast() {
        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo) subject.getPrincipal();
        long uid = info.getUid();

        List<Rab> list = rabService.getPastAuctions(uid);

        return Result.success(getAuctionList(list));
    }

    /**
    * @Description: Get bidder's information
    * @Param: A list of auction which are registered by user
    * @return: RabVO: information about bidder
    */
    private List<RabVO> getAuctionList(List<Rab> list) {
        List<RabVO> ret = new ArrayList<>();

        for(Rab rab : list){
            Auction auction = auctionService.getById(rab.getAid());
            Property property = propertyService.getById(auction.getPid());
            Address address = addressService.getById(property.getPid());
            List<String> photos = FileUtil.getImages(property.getPid());

            RabVO rabVO = new RabVO();
            BeanUtils.copyProperties(auction, rabVO, "startdate" , "enddate");
            BeanUtils.copyProperties(property, rabVO);
            rabVO.setStartdate(auction.getStartdate().toInstant(TimeUtil.getMyZone()).toEpochMilli());
            rabVO.setEnddate(auction.getEnddate().toInstant(TimeUtil.getMyZone()).toEpochMilli());
            rabVO.setAddress(address.getFullAddress());
            rabVO.setPhotos(photos);
            rabVO.setHighestPrice(rab.getHighestPrice());

            if(auction.getStatus().equals("A")||auction.getStatus().equals("S") || auction.getStatus().equals("F")){
                if(auction.getCurrentBid() == 0){
                    rabVO.setCurrentBid(null);
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