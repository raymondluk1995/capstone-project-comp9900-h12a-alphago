package alphago.propertysale.entity.POJO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: propertysale
 * @description: auction status
 * @author: XIAO HAN
 * @create: 2020-10-24 17:19
 **/
@Data
@Accessors(chain = true)
public class AuctionStatus {
    private long aid;
    private String status;
}