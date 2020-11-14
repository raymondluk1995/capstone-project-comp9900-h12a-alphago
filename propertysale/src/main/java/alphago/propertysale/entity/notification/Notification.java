package alphago.propertysale.entity.notification;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *  This class is present user's notification message.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Notification implements Serializable {

    public static final String FINISH = "FINISH";

    private static final long serialVersionUID = 1L;

    @TableId(value = "noti_id", type = IdType.AUTO)
    private Long notiId;

    private String notiType;

    private LocalDateTime createTime;

    private Boolean isRead;

    private byte[] message;

    private Long uid;
}
