package alphago.propertysale.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-09 20:46
 **/
@Data
@Accessors(chain = true)
public class InformationVO {
    private String username;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String avatar;
}