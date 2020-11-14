package alphago.propertysale.entity.POJO;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 *  Plain Java Object of Bid, which can be used to accept data from Front-end and interact with database.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RabAction implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long rabId;

    private Long bidPrice;

    private LocalDateTime bidTime;

    @TableId(value = "action_id", type = IdType.AUTO)
    private Long actionId;


}
