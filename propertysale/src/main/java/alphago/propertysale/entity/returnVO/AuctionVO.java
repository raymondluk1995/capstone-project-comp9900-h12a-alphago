package alphago.propertysale.entity.returnVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-19 21:52
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AuctionVO {
    // auction information
    private long aid;
    private String Rab;

    private String status;
    private long startdate;
    private long enddate;
    private String latestPrice;
    private long bidderNum = 0;
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
}