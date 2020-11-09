package alphago.propertysale.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class History implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "uid")
    private Long uid;

    private Integer bedroomNum;

    private Integer bathroomNum;

    private Integer garageNum;

    private Double lat;

    private Double lng;

    private Integer cnt;

    public static History emptyHistory(){
        return new History().setCnt(0)
                .setBathroomNum(0)
                .setBedroomNum(0)
                .setGarageNum(0)
                .setLat(0D)
                .setLng(0D);
    }


}
