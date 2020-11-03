package alphago.propertysale.entity.returnVO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SearchResVO {
    // rab info
    private long aid;
    private String status;
    private String currentBid; // R -> minimumprice A -> 0?"Not bid" : rab.highestPrice
    private long startdate;
    private long enddate;


    private long bidderNum;


    // property info
    private int bathroomNum;
    private int bedroomNum;
    private int garageNum;
    private int area;
    private String address;
    private List<String> photos;
    private String type;
}
