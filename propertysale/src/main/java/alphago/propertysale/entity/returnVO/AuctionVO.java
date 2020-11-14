package alphago.propertysale.entity.returnVO;

import alphago.propertysale.websocket.BidMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @program: propertysale
 * @description: Value Object of return value of Auction Controller
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AuctionVO {
    // auction information
    private long aid;
    private String Rab;
    private List<BidMsg> history;

    private String status;
    private long startdate;
    private long enddate;
    private Long latestPrice;
    private long bidderNum;

    // owner information
    private long uid;
    private String username;
    private String Firstname;
    private String Lastname;
    private String email;
    private String phone;
    private String avatar;

    //property information
    private String address;
    private String lat;
    private String lng;
    private int bedroomNum;
    private int bathroomNum;
    private int garageNum;
    private int area;
    private String type;
    private String position;
    private String detail;
    private String description;
    private List<String> photos;

    private Long highestPrice;

    private List<RecVO> recommendations;
}