package alphago.propertysale.entity.returnVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @program: propertysale
 * @description: Value Object of Property Address
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