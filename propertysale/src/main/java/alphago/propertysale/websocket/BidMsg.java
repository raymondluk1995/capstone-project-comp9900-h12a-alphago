package alphago.propertysale.websocket;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @program: propertysale
 * @description: Pojo for biding message, which contains the information of
 *               the bidder, bid price and the biding time.
 **/

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BidMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long time;
    private Long uid;
    private String username;
    private Long price;
    private boolean overtime;
    private boolean newBidder;
}