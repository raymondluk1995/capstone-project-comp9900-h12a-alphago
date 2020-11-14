package alphago.propertysale.entity.notification;

import alphago.propertysale.websocket.BidMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.List;

import java.io.Serializable;

/**
 * @program: propertysale
 * @description: Plain object presents notification of finish message
 **/
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class FinishMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    // message status
    private boolean isSuccess;
    private boolean isSeller;
    // auction details
    private long aid;
    private long pid;
    private String address;
    private Long bidPrice;
    // seller details
    private String sellerName;
    private String sellerFullName;
    private String sellerEmail;
    private String sellerPhone;
    // bidder details
    private String bidderName;
    private String bidderFullName;
    private String bidderEmail;
    private String bidderPhone;
    // history
    private List<BidMsg> history;
}