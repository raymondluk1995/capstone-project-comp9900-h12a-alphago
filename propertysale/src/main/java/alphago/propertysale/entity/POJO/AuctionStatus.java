package alphago.propertysale.entity.POJO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: propertysale
 * @description: auction status
 **/
@Data
@Accessors(chain = true)
public class AuctionStatus {
    private long aid;
    private String status;
}