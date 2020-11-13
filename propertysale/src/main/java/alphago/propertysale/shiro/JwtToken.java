package alphago.propertysale.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @program: propertysale
 * @description: jwtToken
 * @author: XIAO HAN
 * @create: 2020-09-26 22:43
 **/
public class JwtToken implements AuthenticationToken {
    private JwtInfo user;

    public JwtToken(JwtInfo user) {
        this.user = user;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public Object getCredentials() {
        return user;
    }
}