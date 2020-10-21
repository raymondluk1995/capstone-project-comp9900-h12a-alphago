package alphago.propertysale.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @program: propertysale
 * @description: PropertyEntity
 * @author: XIAO HAN
 * @create: 2020-10-13 15:19
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Property implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "pid", type = IdType.AUTO)
    private Long pid;

    private Long owner;

    private int bedroomNum;

    private int bathroomNum;

    private int garageNum;

    private int area;

    private String type;

    private String position;

    private String detail;

    private String description;

    @TableField(value = "auction")
    private boolean auction;

    private boolean publicAccess;

}