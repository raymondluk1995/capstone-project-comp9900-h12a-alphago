package alphago.propertysale.entity.returnVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-11-08 09:22
 **/
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class RunningAuctionAddress {

    private double score;

    private String status;

    private Long aid;

    private Long pid;

    private Double bedroomNum;

    private Double bathroomNum;

    private Double garageNum;

    private Double lat;

    private Double lng;

    private String address;

    private String suburb;

    private String state;

    private String postcode;
}