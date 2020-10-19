package alphago.propertysale.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @program: propertysale
 * @description: Auction Entity
 * @author: XIAO HAN
 * @create: 2020-10-15 15:35
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Auction {
    @TableId(type = IdType.AUTO)
    private long aid;
    private long pid;
    @TableField(value = "start_date")
    private Date startdate;
    @TableField(value = "end_date")
    private Date enddate;
    @TableField(value = "reserve_price")
    private String price;
    private String minimumPrice = "1000000";
    private long latestBid;
    private boolean isSuccess = false;
}