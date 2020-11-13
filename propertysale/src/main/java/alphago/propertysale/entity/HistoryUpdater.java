package alphago.propertysale.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @program: propertysale
 * @description: History Apdator for History Mapper
 * @author: XIAO HAN
 * @create: 2020-11-12 21:43
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