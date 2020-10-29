package alphago.propertysale.controller;

import alphago.propertysale.entity.*;
import alphago.propertysale.entity.returnVO.AuctionVO;
import alphago.propertysale.entity.returnVO.RabVO;
import alphago.propertysale.service.*;
import alphago.propertysale.shiro.JwtInfo;
import alphago.propertysale.utils.FileUtil;
import alphago.propertysale.utils.Result;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            }else{
                auctionVO.setRab(null);
            }
        }
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
            rabVO.setStartdate(auction.getStartdate().toEpochSecond(ZoneOffset.UTC));
            rabVO.setEnddate(auction.getEnddate().toEpochSecond(ZoneOffset.UTC));
            BeanUtils.copyProperties(property, rabVO);
            rabVO.setAddress(address.getFullAddress());
            rabVO.setPhotos(photos);
            rabVO.setHighestPrice(rab.getHighestPrice());

            if(auction.getStatus().equals("A")){
                rabVO.setCurrentBid(actionService.getOne(new QueryWrapper<RabAction>()
                        .eq("rab_id", auction.getCurrentBid()))
                        .getBidPrice());
            }else{
                rabVO.setCurrentBid(auction.getMinimumPrice());
            }

            ret.add(rabVO);
        }
        return ret;
    }
}