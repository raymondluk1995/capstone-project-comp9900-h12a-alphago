package alphago.propertysale.entity.POJO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @program: propertysale
 * @description: To change user's History in databse
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode
public class HistoryUpdater {
    private Long uid;

    private Integer bedroomNum;

    private Integer bathroomNum;

    private Integer garageNum;

    private Double lat;

    private Double lng;
}