package alphago.propertysale.controller;

import alphago.propertysale.entity.Auction;
import alphago.propertysale.entity.Property;
import alphago.propertysale.entity.returnVO.AuctionVO;
import alphago.propertysale.service.AuctionService;
import alphago.propertysale.service.PropertyService;
import alphago.propertysale.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // Get auction information based on Property Id
    @RequestMapping("/information/{pid}")
    public Result auctionInformationByProperty(@PathVariable long pid){
        AuctionVO auctionVO = auctionService.getAuctionByProperty(pid);
        return Result.success(auctionVO);
    }

    // Get auction information based on Auction Id
    @RequestMapping("/{aid}")
    public Result auctionInformation(@PathVariable long aid){
        AuctionVO auctionVO = auctionService.getAuctionByAid(aid);
        return Result.success(auctionVO);
    }
}