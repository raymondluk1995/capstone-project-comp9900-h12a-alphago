package alphago.propertysale.controller;


import alphago.propertysale.entity.notification.FinishMessage;
import alphago.propertysale.entity.notification.Notification;
import alphago.propertysale.entity.returnVO.NotificationVO;
import alphago.propertysale.service.NotificationService;
import alphago.propertysale.shiro.JwtInfo;
import alphago.propertysale.utils.Result;
import alphago.propertysale.utils.TimeUtil;
import cn.hutool.core.util.ObjectUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-11-02
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    /**
    * @Description: Count the number of unread notifications.
    */
    @RequestMapping("/unread")
    @RequiresAuthentication
    public Result countUnread(){
        JwtInfo info = (JwtInfo) SecurityUtils.getSubject().getPrincipal();
        long uid = info.getUid();
        return Result.success(notificationService.countUnread(uid));
    }

    /**
    * @Description: Get user's all notifications.
    */
    @RequestMapping("/all")
//    @RequiresAuthentication
    public Result getAll(){
        JwtInfo info = (JwtInfo) SecurityUtils.getSubject().getPrincipal();
        long uid = info.getUid();
        List<NotificationVO> allMessage = notificationService.getAllMessage(uid);
        return Result.success(allMessage);
    }
}
