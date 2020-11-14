package alphago.propertysale.controller;

import alphago.propertysale.entity.POJO.AvatarPorter;
import alphago.propertysale.entity.returnVO.InformationVO;
import alphago.propertysale.entity.returnVO.LoginVO;
import alphago.propertysale.entity.POJO.User;
import alphago.propertysale.rabbit.MessageProducer;
import alphago.propertysale.service.UserService;
import alphago.propertysale.shiro.JwtInfo;
import alphago.propertysale.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @description: Control layer to deal with request sent to User Module
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    MessageProducer messageProducer;

    /**
     * @Description:  User Registration
     *                Add user's information into database and send avatar to Image Server if the provided Validation
     *                Code is correct.
     * @Param:        user: User's basic information
     *                validate: User's validation code
     *                avatar: Avatar uploaded by user
     * @return: "Success" if no expectation, otherwise "Fail" with error messages
     */
    @RequestMapping("/register")
    Result save(User user , String validate , MultipartFile avatar) throws IOException {
        // Check verify code
        if(!CheckCode.checkCode(validate, user.getEmail() , "register"))
            return Result.fail("Verification code is wrong or outdated");

        // Check username
        if(userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername())) != null)
            return Result.fail("Username is exist!");
        // Register
        if(avatar != null)
            user.setAvatarType(FileUtil.getType(avatar.getOriginalFilename()));
        userService.userRegister(user);
        // upload avatar
        if(user.getAvatarType() != null) {
            AvatarPorter porter = new AvatarPorter()
                    .setAvatar(avatar.getBytes())
                    .setName(avatar.getOriginalFilename())
                    .setUid(user.getUid());
            messageProducer.sendMsg(porter, CheckCode.AVATAR);
        }
        return Result.success("Success!");
    }

    /**
    * @Description: User log in
     *             If user's username and password are correct, login and set Jwt token into the Response's Http's head.
    * @Param:      user: username and password provided by user
     *             response: Response servlet
    * @return:     "Success" if no expectation, otherwise "Fail" with error messages
    */
    @RequestMapping("/login")
    @CrossOrigin
    Result login(User user , HttpServletResponse response){
        User logUser = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if(logUser == null) return Result.fail("User is not exist!");
        if(!logUser.getPassword().equals(user.getPassword())) return Result.fail("Wrong password!");
        HashMap<String, String> map = new HashMap<>();
        map.put("username" , user.getUsername());
        map.put("uid" , logUser.getUid().toString());
        String jwt = JWTutil.getJwtToken(map);
        response.setHeader("jwt" , jwt);
        response.setHeader("Access-Control-Expose-Headers", "jwt");
        // get return POJO
        LoginVO ret = new LoginVO()
                        .setUsername(logUser.getUsername())
                        .setFirstname(logUser.getFirstname())
                        .setAvatar(FileUtil.getUserAvatar(logUser));
        return Result.success(ret);
    }

    /**
    * @Description: Login with email address.
    */
    @RequestMapping("/emailLogin")
    Result login(String email , String password , HttpServletResponse response){
        User logUser = userService.getOne(new QueryWrapper<User>().eq("email", email));
        if(logUser == null) return Result.fail("Email is not exist!");
        if(!logUser.getPassword().equals(password)) return Result.fail("Wrong password!");
        HashMap<String, String> map = new HashMap<>();
        map.put("username" , logUser.getUsername());
        map.put("uid" , logUser.getUid().toString());
        String jwt = JWTutil.getJwtToken(map);
        response.setHeader("jwt" , jwt);
        response.setHeader("Access-Control-Expose-Headers", "jwt");
        // get return POJO
        LoginVO ret = new LoginVO()
                .setUsername(logUser.getUsername())
                .setFirstname(logUser.getFirstname())
                .setAvatar(FileUtil.getUserAvatar(logUser));
        return Result.success(ret);
    }

    /**
    * @Description: Reset Password
    */
    @RequestMapping("/reset")
    Result resetPassword(User user, String validate) {
        User targetUser = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if (targetUser == null) return Result.fail("User name arg is wrong!");

        System.out.println("validate = " + validate);
        if (!CheckCode.checkCode(validate, targetUser.getEmail(), CheckCode.RESET)) {
            return Result.fail("Verification code is wrong or outdated.");
        }
        targetUser.setPassword(user.getPassword());
        userService.update(targetUser, new QueryWrapper<User>().eq("username", user.getUsername()));
        return Result.success("reset password successful!");
    }

    /**
    * @Description: Logout
    */
    @RequestMapping("/logout")
    @RequiresAuthentication
    Result logout(){
        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo) subject.getPrincipal();
        System.out.println(info.getUsername());
        System.out.println(info.getUid());
        subject.logout();
        return Result.success("退出成功！");
    }

    /**
    * @Description: Display users' basic information
    */
    @RequestMapping("/information")
    @RequiresAuthentication
    Result information(){
        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo)subject.getPrincipal();
        String username = info.getUsername();
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        InformationVO information = new InformationVO().setUsername(username)
                .setFirstname(user.getFirstname())
                .setLastname(user.getLastname())
                .setPhone(user.getPhone())
                .setEmail(user.getEmail())
                .setAvatar(FileUtil.getUserAvatar(user));
        return Result.success(information);
    }

    /**
    * @Description: Change firstname
    */
    @RequestMapping("/information/changeFirstname")
    @RequiresAuthentication
    Result changeFirstName(String firstname){
        Assert.notNull(firstname);

        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo)subject.getPrincipal();
        String username = info.getUsername();
        userService.update(new User().setFirstname(firstname) , new QueryWrapper<User>().eq("username" , username));
        return Result.success(firstname);
    }


    /**
     * @Description: Change lastname
     */
    @RequestMapping("/information/changeLastname")
    @RequiresAuthentication
    Result changeLastName(String lastname){
        Assert.notNull(lastname);

        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo)subject.getPrincipal();
        String username = info.getUsername();
        userService.update(new User().setLastname(lastname) , new QueryWrapper<User>().eq("username" , username));
        return Result.success(lastname);
    }

    /**
     * @Description: Change email
     */
    @RequestMapping("/information/changeEmail")
    @RequiresAuthentication
    Result changeEmail(String email , String validate){
        Assert.notNull(email);
        Assert.notNull(validate);

        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo)subject.getPrincipal();
        String username = info.getUsername();
        // Check email
        if(userService.emailExist(email)) return Result.fail("Email exist!");
        // Check code
        if(!CheckCode.checkCode(validate, email , "register"))
            return Result.fail("Verification code is wrong or outdated");
        // Update
        userService.update(new User().setEmail(email) , new QueryWrapper<User>().eq("username" , username));
        return Result.success(email);
    }

    /**
     * @Description: Change avatar
     */
    @RequestMapping("/information/changeAvatar")
    @RequiresAuthentication
    Result changeEmail(MultipartFile avatar) throws IOException {
        Assert.notNull(avatar);

        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo)subject.getPrincipal();
        String username = info.getUsername();
        long uid = info.getUid();

        String avatarName = avatar.getOriginalFilename();
        String avatarType = FileUtil.getType(avatarName);
        userService.update(new User().setAvatarType(avatarType) , new QueryWrapper<User>().eq("username" , username));
        AvatarPorter porter = new AvatarPorter()
                .setAvatar(avatar.getBytes())
                .setName(avatarName)
                .setUid(uid);
        messageProducer.sendMsg(porter, CheckCode.AVATAR);
        return Result.success(FileUtil.getUserAvatar(uid , avatarType));
    }
}
