package alphago.propertysale.shiro;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @program: propertysale
 * @description: jwtToken
 * @author: XIAO HAN
 * @create: 2020-09-26 22:43
 **/
public class JwtToken implements AuthenticationToken {
    private String username;

    public JwtToken(String username) {
        this.username = username;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return username;
    }
}