package alphago.propertysale.service.impl;

import alphago.propertysale.entity.History;
import alphago.propertysale.entity.User;
import alphago.propertysale.mapper.HistoryMapper;
import alphago.propertysale.mapper.UserMapper;
import alphago.propertysale.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    UserMapper userMapper;

    @Autowired
    HistoryMapper historyMapper;

    @Override
    public void userRegister(User user) {
        userMapper.insert(user);
        historyMapper.insert(History.emptyHistory().setUid(user.getUid()));
    }

    @Override
    public boolean emailExist(String email) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        return user != null;
    }
}
