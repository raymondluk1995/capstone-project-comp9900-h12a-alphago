package alphago.propertysale.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @program: propertysale
 * @description: Address entity
 * @author: XIAO HAN
 * @create: 2020-10-13 16:07
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "pid")
    private long pid;

    private String lat;

    private String lng;

    private String country;

    private String address;

    private String suburb;

    private String state;

    private String postcode;

    public String getFullAddress(){
        return address + ", " + suburb + ", "+ state + " " + postcode;
    }
}