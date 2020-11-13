package alphago.propertysale.service;

import alphago.propertysale.entity.POJO.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-09-26
 */
public interface UserService extends IService<User> {

    // register user
    void userRegister(User user);

    // Check if email exist
    boolean emailExist(String email);
}
