package alphago.propertysale.shiro;

import alphago.propertysale.utils.JWTutil;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: propertysale
 * @description: 处理JwtToken 的 filter
 * @author: XIAO HAN
 * @create: 2020-09-26 22:29
 **/
@Component
public class JwtFilter extends AuthenticatingFilter {
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String jwt = ((HttpServletRequest) request).getHeader("jwt");
        try {
            DecodedJWT decodedJWT = JWTutil.decodedJWT(jwt);
            String username = decodedJWT.getClaim("username").asString();
            return new JwtToken(username);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        // 获取header中的JwtToken
        String jwt = ((HttpServletRequest) request).getHeader("jwt");
        // 如果没有， 放行
        if(jwt == null) return true;
        // 使用jwt登录
        executeLogin(request , response);
        return false;
    }
}