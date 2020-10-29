package alphago.propertysale.shiro;

import alphago.propertysale.utils.JWTutil;
import alphago.propertysale.utils.Result;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: propertysale
 * @description: 处理JwtToken 的 filter
 * @author: XIAO HAN
 * @create: 2020-09-26 22:29
 **/

/**
* @Description:
* @Param:
* @return:
* @Author: Xiaohan
* @Date: 29/9/20
*/

@Component
public class JwtFilter extends AuthenticatingFilter {
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        String jwt = ((HttpServletRequest) request).getHeader("jwt");
        System.out.println(jwt);
        try {
            DecodedJWT decodedJWT = JWTutil.decodedJWT(jwt);
            String username = decodedJWT.getClaim("username").asString();
            long uid = Long.parseLong(decodedJWT.getClaim("uid").asString());
            return new JwtToken(new JwtInfo(uid , username));
        } catch (RuntimeException e){
            throw e;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 获取header中的JwtToken
        String jwt = ((HttpServletRequest) request).getHeader("jwt");
        // 如果没有， 放行
        if(jwt == null || jwt.equals("") || jwt.equals("null") || jwt.equals("undefined")) return true;
        // 使用jwt登录
        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            //处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            Result r = Result.fail(throwable.getMessage());
            ObjectMapper jsonMapper = new ObjectMapper();
            String json = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(r);
            httpResponse.getOutputStream().println(json);
        } catch (IOException e1) {
        }
        return false;
    }
}