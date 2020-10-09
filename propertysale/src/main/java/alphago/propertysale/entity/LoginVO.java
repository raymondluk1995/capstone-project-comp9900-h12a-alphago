package alphago.propertysale.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: propertysale
 * @description: Return POJO of login controller
 * @author: XIAO HAN
 * @create: 2020-10-08 15:46
 **/
@Data
@Accessors(chain = true)
public class LoginVO {
    private String username;
    private String firstname;
    private String avatar;
}