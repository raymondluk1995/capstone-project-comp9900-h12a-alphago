package alphago.propertysale.service.impl;

import alphago.propertysale.entity.User;
import alphago.propertysale.mapper.UserMapper;
import alphago.propertysale.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-09-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
