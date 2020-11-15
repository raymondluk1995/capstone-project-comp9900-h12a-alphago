package alphago.propertysale.controller;

import alphago.propertysale.entity.POJO.Address;
import alphago.propertysale.entity.POJO.Auction;
import alphago.propertysale.entity.POJO.AuctionStatus;
import alphago.propertysale.entity.POJO.Property;
import alphago.propertysale.entity.returnVO.PropertyVO;
import alphago.propertysale.service.AddressService;
import alphago.propertysale.service.AuctionService;
import alphago.propertysale.service.PropertyService;
import alphago.propertysale.shiro.JwtInfo;
import alphago.propertysale.utils.FileUtil;
import alphago.propertysale.utils.Result;
import alphago.propertysale.utils.TimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: propertysale
 * @description: Controller for Property
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

    /**
    * @Description: User add new Property
    * @Param: property: User's Property from Front-end
     *        address:  Property's address information
     *        photos:   Property's photos
     *        auction:  Property's auction information. null, if property not registered as an auction.
    * @return: "Success" if no expectation, otherwise "Fail" with error messages
    */
    @RequestMapping("/registration")
    @RequiresAuthentication
    public Result propertyRegister(Property property, Address address, MultipartFile[] photos, Auction auction) throws IOException, InterruptedException {
        // get owner's id
        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo) subject.getPrincipal();
        long ownerId = info.getUid();

        // Check if start date is valid
        if(property.isAuction() && auction.getStartdate().toInstant(TimeUtil.getMyZone()).toEpochMilli() -
                System.currentTimeMillis() < 0) return Result.fail("Start Time Is Not Valid!");
        if(property.isAuction() && auction.getEnddate().toInstant(TimeUtil.getMyZone()).toEpochMilli() -
                auction.getStartdate().toInstant(TimeUtil.getMyZone()).toEpochMilli() <= 0){
            return Result.fail("End Date Must After Start Date!");
        }
        if(photos.length == 0) return Result.fail("No photos!");

        propertyService.propertyRegister(ownerId, property, address, photos, auction);

        return Result.success("success");
    }

    /**
    * @Description: Get all of user's valid properties
    * @return: List of Properties with information of basic, address and auction information.
    */
    @RequiresAuthentication
    @RequestMapping("/propties")
    public Result information(){
        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo) subject.getPrincipal();
        long owner = info.getUid();

        // Get all properties based on use's Id
        List<Property> properties = propertyService.list(new QueryWrapper<Property>().
                eq("owner", owner).eq("deleted", false));
        if(properties.size() == 0) return Result.fail("You haven't post any property!");
        List<PropertyVO> voList = new ArrayList<>();

        // Get each property's address
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

    /**
    * @Description: Register new auction with exist Property
    * @Param:      auction: Auction information: Start and End date, Reserve and Minimum Price
    * @return: "Success" if no expectation, otherwise "Fail" with error messages
    */
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


    /**
    * @Description: Delete Property which has not been registered an auction.
    * @Param:   pid: The property's ID, which is about to be deleted.
    * @return: "Success" if no expectation, otherwise "Fail" with error messages
    */
    @RequestMapping("/delete")
    @RequiresAuthentication
    public Result deleteProperty(String pid){
        Property p = propertyService.getById(pid);
        if(p == null) return Result.fail("No such Property!");
        if(p.getDeleted()) return Result.fail("Property P"+ pid + " has already be deleted");

        propertyService.update(new UpdateWrapper<Property>().eq("pid", pid).set("deleted", true));

        return Result.success("Remove Property: " + pid);
    }
    /**
    * @Description: Cancel registered Auction
    * @Param:   aid: Auction Id
     *          pid: Property's Id
    * @return: "Success" if no expectation, otherwise "Fail" with error messages
    */
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