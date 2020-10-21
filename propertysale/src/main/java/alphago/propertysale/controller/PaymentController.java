package alphago.propertysale.controller;


import alphago.propertysale.entity.Payment;
import alphago.propertysale.shiro.JwtInfo;
import alphago.propertysale.utils.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-10-21
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    /**
    * @Description: Get User's cards from Payment table
    * @Param:
    * @return: List of User's cards(payments)
    */
    @RequiresAuthentication
    @RequestMapping("/get")
    public Result getUserPayments(){
        // get user's uid
        JwtInfo info = (JwtInfo)SecurityUtils.getSubject();
        long uid = info.getUid();
    }

    /**
    * @Description: Add new payment method
    * @Param:
    */
    @RequiresAuthentication
    @RequestMapping("/add")
    public Result addNewCard(Payment payment){
        // get user's uid
        JwtInfo info = (JwtInfo)SecurityUtils.getSubject();
        long uid = info.getUid();
    }
}
