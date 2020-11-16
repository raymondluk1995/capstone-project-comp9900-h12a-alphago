package alphago.propertysale.controller;


import alphago.propertysale.entity.POJO.Rab;
import alphago.propertysale.service.AuctionService;
import alphago.propertysale.service.RabService;
import alphago.propertysale.shiro.JwtInfo;
import alphago.propertysale.utils.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
* @description: Control layer to deal with request sent to Registered Auction Bidder(RAB) Module
*/
@RestController
@RequestMapping("/rab")
public class RabController {
    @Autowired
    AuctionService auctionService;

    @Autowired
    RabService rabService;   // User register new auction

    /**
    * @Description: Potential Buyer register an auction to be the rab of its' RAB
    * @Param:   rab: Registration Information
    * @return: "Success" if no expectation, otherwise "Fail" with error messages
    */
    @RequestMapping("/register")
    @RequiresAuthentication
    public Result register(Rab rab){
        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo)subject.getPrincipal();
        long uid = info.getUid();

        // Check already registered
        long aid = rab.getAid();
        if(rabService.isRegistered(uid, aid)) return Result.fail("You have already registered Auction: " + aid);

        //Check auction status
        rab.setUid(uid)
            .setHighestPrice(rab.getInitPrice())
            .setRegisterTime(LocalDateTime.now());

        rabService.rabRegister(rab);

        Map<String, Object> ret = new HashMap<>();
        ret.put("id", rab.getRabId());
        ret.put("price", rab.getInitPrice());
        return Result.success(ret);
    }
}

