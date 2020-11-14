package alphago.propertysale.service;

import alphago.propertysale.entity.POJO.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: Interface of service in User Controller
 */
public interface UserService extends IService<User> {

    // register user
    void userRegister(User user);

    // Check if email exist
    boolean emailExist(String email);
}
