package alphago.propertysale.controller;

import alphago.propertysale.utils.CheckCode;
import alphago.propertysale.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: propertysale
 * @description: Controller for dealing with Verification Code
 * @author: XIAO HAN
 * @create: 2020-10-06 21:58
 **/
@RestController
@RequestMapping("/verify")
public class VerificationController {
    @Autowired
    private JavaMailSender mailSender;

    @RequestMapping("/register")
    Result sendRegisterCode(String email){
        CheckCode.sendCode(mailSender , email , "register");
        return Result.success("成功发送");
    }

    @RequestMapping("/reset")
    Result sendResetCode(String email){
        CheckCode.sendCode(mailSender , email , "reset");
        return Result.success("成功发送");
    }
}