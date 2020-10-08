package alphago.propertysale.controller;

import alphago.propertysale.entity.User;
import alphago.propertysale.service.UserService;
import alphago.propertysale.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-09-26
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    /**
    * @Description:  User Registration
    * @return:
    * @Author: Xiaohan
    * @Date: 6/10/20
    */
    @RequestMapping("/register")
    Result save(User user , String validate , MultipartFile avatar , HttpServletRequest request) throws IOException {
        // Check verify code
        if(!CheckCode.checkCode(validate, user.getEmail() , "register")) return Result.fail("验证码错误或失效");

        // Check username
        User resUser = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if(resUser != null) return Result.fail("Username is exist!");
        // Register
        userService.save(user);
        // get User Id
        resUser = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()).select("uid"));
        // upload avatar
        if(avatar != null)
            FileUtil.uploadAvatar(avatar , resUser.getUid().toString());
        return Result.success("注册成功!");
    }

    @RequestMapping("/login")
    @CrossOrigin
    Result login(User user , HttpServletResponse response , HttpServletRequest request){
        User logUser = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if(logUser == null) return Result.fail("没有此用户");
        if(!logUser.getPassword().equals(user.getPassword())) return Result.fail("密码错误");
        HashMap<String, String> map = new HashMap<>();
        map.put("username" , user.getUsername());
        String jwt = JWTutil.getJwtToken(map);
        response.setHeader("jwt" , jwt);
        response.setHeader("Access-Control-Expose-Headers", "jwt");
        return Result.success(logUser);
    }


    @RequestMapping("/logout")
    @RequiresAuthentication
    Result logout(){
        Subject subject = SecurityUtils.getSubject();
        System.out.println(subject.getPrincipal());
        subject.logout();
        return Result.success("退出成功！");
    }
}
