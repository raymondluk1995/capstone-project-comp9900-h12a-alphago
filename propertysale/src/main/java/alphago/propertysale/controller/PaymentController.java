package alphago.propertysale.controller;


import alphago.propertysale.entity.POJO.Payment;
import alphago.propertysale.service.PaymentService;
import alphago.propertysale.shiro.JwtInfo;
import alphago.propertysale.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: Controller to deal with payment's request
 *              Springboot will dispatch request to exact
 *              function based on the URL.
 **/
@RestController
@RequestMapping("/payment")
public class PaymentController {

    /**
     * @Description: Get User's cards from Payment table
     * @Param:
     * @return: List of User's cards(payments)
     */
    @Autowired
    private PaymentService paymentService;

    @RequiresAuthentication
    @RequestMapping("/get")
    public Result getUserPayments() {
        // get user's uid
        JwtInfo info = (JwtInfo) SecurityUtils.getSubject().getPrincipal();
        long uid = info.getUid();
        List<Payment> list = paymentService.list(new QueryWrapper<Payment>().eq("uid", uid));
        return Result.success(list);
    }


    /**
    * @Description: Add new payment method
    * @Param: Card details sent from Front-end
    */
    @RequiresAuthentication
    @RequestMapping("/add")
    public Result addNewCard(Payment payment) {
        // get user's uid
        JwtInfo info = (JwtInfo) SecurityUtils.getSubject().getPrincipal();
        long uid = info.getUid();
        // no duplicate card.
        Payment temp = paymentService.getOne(new QueryWrapper<Payment>().eq("card_Number", payment.getCardNumber()));
        if (temp != null) {
            return Result.fail("You have this card already. Try other one.");
        }
        payment.setUid(uid);
        paymentService.save(payment);
        return Result.success(payment);
    }
}
