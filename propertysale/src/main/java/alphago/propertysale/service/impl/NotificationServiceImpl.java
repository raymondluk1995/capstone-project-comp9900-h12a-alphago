package alphago.propertysale.service.impl;

import alphago.propertysale.entity.notification.Notification;
import alphago.propertysale.entity.returnVO.NotificationVO;
import alphago.propertysale.mapper.NotificationMapper;
import alphago.propertysale.service.NotificationService;
import alphago.propertysale.utils.TimeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-11-02
 */
@Service
@Transactional
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Autowired
    NotificationMapper notificationMapper;

    @Override
    public int countUnread(long uid) {
        return notificationMapper.selectCount(
                new QueryWrapper<Notification>()
                .eq("uid", uid)
                .eq("is_read", true)
        );
    }

    @Override
    public void addNewNotification(Notification notification) {
        notificationMapper.addMessage(notification);
    }

    @Override
    public List<NotificationVO> getAllMessage(long uid) {
        List<NotificationVO> ret = new ArrayList<>();
        List<Notification> notifications = notificationMapper.selectList(new QueryWrapper<Notification>().eq("uid", uid));

        for(Notification notification : notifications){
            NotificationVO notificationVO = new NotificationVO();
            BeanUtils.copyProperties(notification, notificationVO, "message", "createTime");
            notificationVO.setMessage(ObjectUtil.deserialize(notification.getMessage()));
            notificationVO.setCreateTime(notification.getCreateTime().toInstant(TimeUtil.getMyZone()).toEpochMilli());
            ret.add(notificationVO);
        }

        return ret;
    }
}
