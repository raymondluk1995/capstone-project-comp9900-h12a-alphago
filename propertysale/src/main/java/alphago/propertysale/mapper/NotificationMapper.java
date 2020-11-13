package alphago.propertysale.mapper;

import alphago.propertysale.entity.notification.Notification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-11-02
 */
public interface NotificationMapper extends BaseMapper<Notification> {

    @Options(useGeneratedKeys = true, keyProperty = "notiId" ,keyColumn = "noti_id")
    @Insert("INSERT into notification  (noti_type , create_time , `is_read` , uid, message) VALUES (#{notiType}, #{createTime}, #{isRead}, #{uid}, #{message});")
    void addMessage(Notification notification);
}
