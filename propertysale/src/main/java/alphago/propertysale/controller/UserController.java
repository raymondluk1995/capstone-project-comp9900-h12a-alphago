package alphago.propertysale.controller;

import alphago.propertysale.entity.User;
import alphago.propertysale.service.UserService;
import alphago.propertysale.utils.JWTutil;
import alphago.propertysale.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @RequestMapping("/save")
    Result save(User user){
        userService.save(user);
        return Result.success(user);
    }

//    @CrossOrigin
    @RequestMapping("/login")
    Result login(User user , HttpServletResponse response , HttpServletRequest request){
//        Assert.notNull(user.getEmail());
        User logUser = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if(logUser == null) return Result.fail("没有此用户");
        if(!logUser.getPassword().equals(user.getPassword())) return Result.fail("密码错误");
        HashMap<String, String> map = new HashMap<>();
        map.put("username" , user.getUsername());
        String jwt = JWTutil.getJwtToken(map);
        response.setHeader("jwt" , jwt);
        response.setHeader("Access-Control-Expose-Headers", "jwt");
        return Result.success("登录成功");
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
