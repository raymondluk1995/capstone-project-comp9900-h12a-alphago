package alphago.propertysale.controller;

import alphago.propertysale.entity.POJO.RabAction;
import alphago.propertysale.service.RabActionService;
import alphago.propertysale.service.UserService;
import alphago.propertysale.utils.Result;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @program: propertysale
 * @description: Control layer to deal with request sent to Bid Module
 **/
@RestController
public class BidController {
    @Autowired
    RabActionService rabActionService;
    @Autowired
    UserService userService;

    /**
    * @Description: Deal with user's bid
    * @Param: RabAction: Bid information sent from Front-end
    */
    @RequiresAuthentication
    @RequestMapping("/bid")
    public Result bid(RabAction rabAction) {
        // Set Bid Time
        rabAction.setBidTime(LocalDateTime.now());
        rabActionService.bid(rabAction);

        return Result.success(rabAction);
    }
}