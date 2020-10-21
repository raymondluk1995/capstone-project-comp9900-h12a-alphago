package alphago.propertysale.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-10-20
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

    private String initPrice;

    private String highestPrice;


}
