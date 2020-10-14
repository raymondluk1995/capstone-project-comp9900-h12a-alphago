package alphago.propertysale.entity.returnVO;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.List;
/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-14 10:53
 **/
@Data
@Accessors(chain = true)
public class PropertyVO {
    private long id;
    private int area;
    private List<String> photo;
    private String address;
    private int bathroomNum;
    private int bedroomNum;
    private int garageNum;
    private String type;
}