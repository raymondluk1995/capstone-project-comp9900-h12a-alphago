package alphago.propertysale.entity.returnVO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: propertysale
 * @description: Return Value Object of login controller
 **/
@Data
@Accessors(chain = true)
public class LoginVO {
    private String username;
    private String firstname;
    private String avatar;
}