package alphago.propertysale.controller;


import alphago.propertysale.entity.notification.Notification;
import alphago.propertysale.entity.returnVO.NotificationVO;
import alphago.propertysale.service.NotificationService;
import alphago.propertysale.shiro.JwtInfo;
import alphago.propertysale.utils.Result;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
* @Description: Control layer to deal with request sent to notification Module
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
    * @Description: Change notification's status
     *  From unread to read
    */
    @RequestMapping("/isRead")
    @RequiresAuthentication
    public Result isRead(long notiId){
        notificationService.update(new UpdateWrapper<Notification>().eq("noti_id", notiId).set("is_read", true));
        return Result.success("");
    }

    /**
    * @Description: Get all of user's notifications.
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
