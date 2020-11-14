package alphago.propertysale.shiro;

import alphago.propertysale.entity.POJO.User;
import alphago.propertysale.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: propertysale
 * @description: Custom realm for authentication
 **/
public class MyRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    /**
    * Authorization
    */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
    /**
     * Authentication
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) token;
        JwtInfo info = (JwtInfo)jwtToken.getPrincipal();
        String username = info.getUsername();
        // get user based on the username in JwtToken
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        if(user == null){
            throw new UnknownAccountException("username not exist!");
        }
        return new SimpleAuthenticationInfo(info , jwtToken.getCredentials() , getName());
    }
}