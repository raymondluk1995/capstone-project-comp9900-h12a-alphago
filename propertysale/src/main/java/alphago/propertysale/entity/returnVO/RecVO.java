package alphago.propertysale.entity.returnVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-11-07 23:45
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode
public class RecVO {

    private Long aid;
    private String status;

    private String address;
    private String suburb;
    private String state;
    private String postcode;

    private Integer bathroomNum;
    private Integer bedroomNum;
    private Integer garageNum;

    private Double lat;
    private Double lng;

    private String photo;
}