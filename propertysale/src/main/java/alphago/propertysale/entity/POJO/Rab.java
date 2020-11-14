package alphago.propertysale.entity.POJO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *  Plain Java Object of Registered Auction Bidder, which can be used to accept data from Front-end and interact with database.
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Rab implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "rab_id", type = IdType.AUTO)
    private Long rabId;

    private Long aid;

    private Long uid;

    private LocalDateTime registerTime;

    private Long initPrice;

    private Long highestPrice;

    private Long paymentId;
}
