package alphago.propertysale.controller;


import alphago.propertysale.entity.Auction;
import alphago.propertysale.entity.Rab;
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

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-10-20
 */
@RestController
@RequestMapping("/rab")
public class RabController {
    @Autowired
    AuctionService auctionService;

    @Autowired
    RabService rabService;   // User register new auction

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

        return Result.success("Register successful! id: " + rab.getRabId());
    }
}

