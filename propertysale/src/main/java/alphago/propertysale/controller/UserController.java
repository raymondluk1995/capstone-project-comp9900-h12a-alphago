package alphago.propertysale.controller;


import alphago.propertysale.service.UserService;
import alphago.propertysale.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping("/hello")
    Result hello(){
        return Result.success("hello world");
    }

}
