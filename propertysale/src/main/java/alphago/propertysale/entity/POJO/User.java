package alphago.propertysale.entity.POJO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 *  Plain Java Object of User, which can be used to accept data from Front-end and interact with database.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uid", type = IdType.AUTO)
    private Long uid;

    private String username;

    private String firstname;

    private String lastname;

    private String password;

    private String salt;

    private String phone;

    private String email;

    private String avatarType;

    public String getFullName(){
        return firstname + " " + lastname;
    }
}
