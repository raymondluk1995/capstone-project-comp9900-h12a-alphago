package alphago.propertysale.service;

import alphago.propertysale.entity.notification.Notification;
import alphago.propertysale.entity.returnVO.NotificationVO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.*;
/**
 * @Description: Interface of service in Notification Controller
 */
public interface NotificationService extends IService<Notification> {

    int countUnread(long uid);

    void addNewNotification(Notification notification);

    List<NotificationVO> getAllMessage(long uid);
}
