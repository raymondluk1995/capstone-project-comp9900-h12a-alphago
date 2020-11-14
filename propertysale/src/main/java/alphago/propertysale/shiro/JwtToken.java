package alphago.propertysale.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @program: propertysale
 * @description: jwtToken to identify an authenticated user
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