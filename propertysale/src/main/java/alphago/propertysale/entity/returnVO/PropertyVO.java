package alphago.propertysale.entity.returnVO;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.List;
/**
 * @program: propertysale
 * @description: Return value Object of Property
 **/
@Data
@Accessors(chain = true)
public class PropertyVO {
    private long pid;
    private long aid;
    private int area;
    private List<String> photos;
    private String address;
    private int bathroomNum;
    private int bedroomNum;
    private int garageNum;
    private String type;
    private String position;
    private String description;
    private String detail;
    private String status;
}