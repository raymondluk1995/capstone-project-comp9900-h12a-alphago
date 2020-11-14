package alphago.propertysale.controller;

import alphago.propertysale.entity.POJO.User;
import alphago.propertysale.rabbit.MessageProducer;
import alphago.propertysale.service.UserService;
import alphago.propertysale.utils.CheckCode;
import alphago.propertysale.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: propertysale
 * @description: Controller for dealing with Verification Code
 **/
@RestController
@RequestMapping("/verify")
public class VerificationController {
    @Autowired
    UserService userService;
    @Autowired
    MessageProducer messageProducer;
    /**
     *  Send Verification code for registration
     */
    @RequestMapping("/register")
    Result sendRegisterCode(String email){
        if(userService.emailExist(email)) return Result.fail("Email exist! Please Change!");
        messageProducer.sendMsg(email , CheckCode.REGISTER);
        return Result.success("Success");
    }

    /**
     *  Send Verification code for reset password
     */
    @RequestMapping("/reset")
    Result sendResetCode(String username){
        // get user based on the username
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username).select("email"));
        if(user == null) return Result.fail("User is not exist!");
        String email = user.getEmail();
        // send email if user exist
        messageProducer.sendMsg(email , CheckCode.RESET);
        return Result.success("Success");
    }
}