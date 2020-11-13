package alphago.propertysale.entity.returnVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-11-03 15:24
 **/
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class NotificationVO {

    private long notiId;
    private String notiType;
    private long createTime;
    private Boolean isRead;
    private Object message;
    private long uid;
}