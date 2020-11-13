package alphago.propertysale.entity.POJO;

import java.io.Serializable;

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

    private Double bedroomNum;

    private Double bathroomNum;

    private Double garageNum;

    private Double lat;

    private Double lng;

    private Double bedroomCnt;

    private Double bathroomCnt;

    private Double garageCnt;

    private Double latCnt;

    private Double lngCnt;

    public static History emptyHistory(){
        return new History()
                .setBathroomNum(0D)
                .setBedroomNum(0D)
                .setGarageNum(0D)
                .setLat(0D)
                .setLng(0D)
                .setBathroomCnt(0D)
                .setBedroomCnt(0D)
                .setGarageCnt(0D)
                .setLatCnt(0D)
                .setLngCnt(0D);
    }


}
