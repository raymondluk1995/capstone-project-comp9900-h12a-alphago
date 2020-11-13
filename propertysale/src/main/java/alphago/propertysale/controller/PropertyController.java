package alphago.propertysale.controller;

import alphago.propertysale.entity.*;
import alphago.propertysale.entity.returnVO.PropertyVO;
import alphago.propertysale.rabbit.MessageProducer;
import alphago.propertysale.service.AddressService;
import alphago.propertysale.service.AuctionService;
import alphago.propertysale.service.PropertyService;
import alphago.propertysale.shiro.JwtInfo;
import alphago.propertysale.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: propertysale
 * @description: Controller for Property
 * @author: XIAO HAN
 * @create: 2020-10-13 15:34
 **/
@RestController
@RequestMapping("/property")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private AuctionService auctionService;
    @Value("${remote.url}")
    private String url;
    // property registration
    @RequestMapping("/registration")
    @RequiresAuthentication
    public Result propertyRegister(Property property, Address address, MultipartFile[] photos, Auction auction) throws IOException, InterruptedException {
//         get owner's id
        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo) subject.getPrincipal();
        long ownerId = info.getUid();

        if(property.isAuction() && auction.getStartdate().toInstant(TimeUtil.getMyZone()).toEpochMilli() -
                System.currentTimeMillis() < 0) return Result.fail("Start Time Is Not Valid!");
        if(auction.getEnddate().toInstant(TimeUtil.getMyZone()).toEpochMilli() -
                auction.getStartdate().toInstant(TimeUtil.getMyZone()).toEpochMilli() <= 0){
            return Result.fail("End Date Must After Start Date!");
        }
        if(photos.length == 0) return Result.fail("No photos!");

        propertyService.propertyRegister(ownerId, property, address, photos, auction);

        return Result.success("success");
    }

    @RequiresAuthentication
    @RequestMapping("/propties")
    public Result information(){
        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo) subject.getPrincipal();
        long owner = info.getUid();
        List<Property> properties = propertyService.list(new QueryWrapper<Property>().
                eq("owner", owner));
        if(properties.size() == 0) return Result.fail("You haven't post any property!");
        List<PropertyVO> voList = new ArrayList<>();

        for(Property property : properties){
            Address address = addressService.getById(property.getPid());
            PropertyVO propertyVO = new PropertyVO();
            BeanUtils.copyProperties(property , propertyVO);
            propertyVO.setAddress(address.getFullAddress());
            propertyVO.setPhotos(FileUtil.getImages(property.getPid()));
            // add status
            if(property.isAuction()){
                AuctionStatus status = auctionService.getAuctionStatus(property.getPid());
                BeanUtils.copyProperties(status, propertyVO);
            }else{
                propertyVO.setStatus("N");
            }
            voList.add(propertyVO);
        }
        return Result.success(voList);
    }

    @RequestMapping("/newAuction")
    @RequiresAuthentication
    public Result register(Auction auction){
        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo) subject.getPrincipal();
        long seller = info.getUid();

        if(auction.getStartdate().toInstant(TimeUtil.getMyZone()).toEpochMilli() -
                System.currentTimeMillis() < 0) return Result.fail("Start Time Is Not Valid!");

        if(auction.getEnddate().toInstant(TimeUtil.getMyZone()).toEpochMilli() -
                auction.getStartdate().toInstant(TimeUtil.getMyZone()).toEpochMilli() <= 0){
            return Result.fail("End Date Must After Start Date!");
        }

        // Check
        long pid = auction.getPid();
        if(propertyService.getById(pid).isAuction()) return Result.fail("Property has already registered!");
        propertyService.propertyNewAuction(auction, seller);

        return Result.success("Add new Auction!");
    }

    @RequestMapping("/delete")
    @RequiresAuthentication
    public Result deleteProperty(String pid){
        if(propertyService.getById(pid) == null) return Result.fail("Already deleted!");

        propertyService.removeById(pid);
        // Delete property photos;
//        messageProducer.sendMsg(pid, CheckCode.REMOVE);

        return Result.success("Remove Property: " + pid);
    }

    // Cancel registered Auction
    @RequestMapping("/cancel")
    @RequiresAuthentication
    public Result cancel(long aid, long pid){
        // Check already cancel
        if(!propertyService.getById(pid).isAuction()){
            return Result.fail("Already canceled!");
        }
        // Check status
        if(auctionService.getAuctionStatus(pid).getStatus().equals(Auction.AUCTION)){
            return Result.fail("Auction already started!");
        }
        // Cancel
        auctionService.auctionCancel(pid, aid);

        return Result.success("success!");
    }
}