package alphago.propertysale.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)

public class RAB implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "rab_id", type = IdType.AUTO)
    private Long rab_id;

    private Long aid;

    private Long uid;
}
