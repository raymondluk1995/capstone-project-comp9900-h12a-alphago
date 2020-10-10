package alphago.propertysale.utils;

import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.servlet.http.Cookie;
import java.util.concurrent.TimeUnit;

/**
 * @program: propertysale
 * @description: Check Verification Code
 * @author: XIAO HAN
 * @create: 2020-10-07 13:39
 **/
public class CheckCode {
    private static final int timeout = 10;
    public static final String REGISTER = "register";
    public static final String RESET = "reset";
    public static final String AVATAR = "avatar";

    public static void sendCode(JavaMailSender mailSender , String email , String usage){
        RedisTemplate valueRedis = RedisUtil.valueRedis();
        // generate id and code
        String code = RandomUtil.randomString(6);
        System.out.println(code);
        // put verify code into redis
        valueRedis.opsForValue().set(usage+email , code , timeout , TimeUnit.MINUTES);
        // send Email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("zxhhaha@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setSubject(usage);
        mailMessage.setText(code);

        mailSender.send(mailMessage);
    }

    /**
    * @Description: Check if the code is valid
    * @Author: Xiaohan
    * @Date: 7/10/20
    */
    public static boolean checkCode(String code, String email , String usage){
        RedisTemplate redisTemplate = RedisUtil.valueRedis();
        String check = (String)redisTemplate.opsForValue().get(usage + email);
        if(check == null || !check.equals(code)) return false;
        return true;
    }
}