package alphago.propertysale.entity.returnVO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: propertysale
 * @description: Value object of user information
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