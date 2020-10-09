package alphago.propertysale.controller;

import alphago.propertysale.entity.AvatarPorter;
import alphago.propertysale.entity.LoginVO;
import alphago.propertysale.entity.User;
import alphago.propertysale.rabbit.MessageProducer;
import alphago.propertysale.service.UserService;
import alphago.propertysale.utils.*;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    ServerUtil serverUtil;

    @Autowired
    MessageProducer messageProducer;
    @Value("${remote.url}")
    private String remote;
    /**
     * @Description:  User Registration
     * @return:
     * @Author: Xiaohan
     * @Date: 6/10/20
     */
    @RequestMapping("/register")
    Result save(User user , String validate , MultipartFile avatar , HttpServletRequest request , HttpServletResponse response) throws IOException, ServletException {
        // Check verify code
        if(!CheckCode.checkCode(validate, user.getEmail() , "register"))
            return Result.fail("Verification code is wrong or outdated");

        // Check username
        User resUser = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if(resUser != null) return Result.fail("Username is exist!");
        // Register
        if(avatar != null)
            user.setAvatarType(FileUtil.getType(avatar.getOriginalFilename()));
        userService.save(user);
        // get User Id
        resUser = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        // upload avatar
        if(resUser.getAvatarType() != null || !resUser.getAvatarType().equals("")) {
            AvatarPorter porter = new AvatarPorter()
                    .setAvatar(avatar.getBytes())
                    .setType(resUser.getAvatarType())
                    .setUid(resUser.getUid());
            messageProducer.sendMsg(porter, CheckCode.AVATAR);
        }
        return Result.success("注册成功!");
    }

    @RequestMapping("/login")
    @CrossOrigin
    Result login(User user , HttpServletResponse response , HttpServletRequest request){
        User logUser = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if(logUser == null) return Result.fail("User is not exist!");
        if(!logUser.getPassword().equals(user.getPassword())) return Result.fail("Wrong password!");
        HashMap<String, String> map = new HashMap<>();
        map.put("username" , user.getUsername());
        String jwt = JWTutil.getJwtToken(map);
        response.setHeader("jwt" , jwt);
        response.setHeader("Access-Control-Expose-Headers", "jwt");
        // get return POJO
        LoginVO ret = new LoginVO()
                        .setUsername(logUser.getUsername())
                        .setFirstname(logUser.getFirstname())
                        .setAvatar(remote + FileUtil.getUserAvatar(logUser));
        return Result.success(ret);
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
