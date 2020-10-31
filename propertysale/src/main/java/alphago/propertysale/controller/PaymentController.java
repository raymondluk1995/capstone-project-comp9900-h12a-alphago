package alphago.propertysale.controller;


import alphago.propertysale.entity.Payment;
import alphago.propertysale.service.PaymentService;
import alphago.propertysale.shiro.JwtInfo;
import alphago.propertysale.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
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
    @Autowired
    private PaymentService paymentService;

    @RequiresAuthentication
    @RequestMapping("/get")
    public Result getUserPayments() {
        // get user's uid
        JwtInfo info = (JwtInfo) SecurityUtils.getSubject().getPrincipal();
        long uid = info.getUid();
        List<Payment> list = paymentService.list(new QueryWrapper<Payment>().eq("uid", uid));
        if (list.size() == 0) {
            return Result.fail("You have no card(s)!");
        }
        return Result.success(list);
    }

    //
//    /**
//    * @Description: Add new payment method
//    * @Param:
//    */
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
        return Result.success("Add card successfully!");
    }
}
