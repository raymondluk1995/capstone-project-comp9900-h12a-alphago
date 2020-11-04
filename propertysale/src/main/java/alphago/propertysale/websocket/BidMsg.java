package alphago.propertysale.websocket;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-30 17:55
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BidMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long time;
    private Long uid;
    private String username;
    private String price;
    private boolean overtime;

}