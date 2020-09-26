package alphago.propertysale.shiro;

import alphago.propertysale.entity.User;
import alphago.propertysale.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-09-26 22:15
 **/
public class MyRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    /**
    * 处理授权
    */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
    /**
     * 处理验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) token;
        String username = (String)jwtToken.getPrincipal();
        // get user based on the username in JwtToken
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        if(user == null){
            throw new UnknownAccountException("username not exist!");
        }
        return new SimpleAuthenticationInfo(user , jwtToken.getCredentials() , getName());
    }
}