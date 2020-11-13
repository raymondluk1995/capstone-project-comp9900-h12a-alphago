package alphago.propertysale.entity.returnVO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @program: propertysale
 * @description: Auctions registered by user(rab)
 * @author: XIAO HAN
 * @create: 2020-10-25 14:45
 **/
@Data
@Accessors(chain = true)
public class RabVO {
    private long aid;
    private String status; // R  A

    private int area;
    private List<String> photos;
    private String address;
    private int bathroomNum;
    private int bedroomNum;
    private int garageNum;
    private String type;

    private long startdate;
    private long enddate;

    private Long currentBid;  //R: minimum price
    private Long highestPrice;
}