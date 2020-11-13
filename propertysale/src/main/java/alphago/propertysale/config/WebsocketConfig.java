package alphago.propertysale.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.ArrayList;

/**
 * @program: propertysale
 * @description: Websocket
 * @author: XIAO HAN
 * @create: 2020-10-16 17:20
 **/
@Configuration
public class WebsocketConfig extends ServerEndpointConfig.Configurator {
    // 修改握手
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        Subject subject = SecurityUtils.getSubject();
        sec.getUserProperties().put("isAuth" ,subject.isAuthenticated());
        super.modifyHandshake(sec,request,response);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}