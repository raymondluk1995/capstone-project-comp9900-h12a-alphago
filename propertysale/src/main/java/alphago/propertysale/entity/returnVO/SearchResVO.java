package alphago.propertysale.entity.returnVO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class SearchResVO {

    // select rab.aid, rab.status..., property.bathroom... from rab, property where (rab.pid = property.pid) and
    // xxx like xxx
    // rab info
    private Long pid;
    private Long aid;
    private String status;
    private String currentBid; // R -> minimumprice A -> 0?"Not bid" : rab.highestPrice
    private LocalDateTime startdate;
    private LocalDateTime enddate;


    private Long bidderNum;


    // property info
    private Integer bathroomNum;
    private Integer bedroomNum;
    private Integer garageNum;
    private Integer area;
    private String address;
    private List<String> photos;
    private String type;
}
