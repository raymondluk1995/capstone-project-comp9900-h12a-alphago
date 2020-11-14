package alphago.propertysale.entity.POJO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 *  Plain Java Object of Property, which can be used to accept data from Front-end and interact with database.
 */
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

    @TableLogic
    private Boolean deleted = false;
}