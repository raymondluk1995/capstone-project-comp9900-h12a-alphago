package com.zxh.Config;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: chatroot
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-15 19:30
 **/
@ServerEndpoint("/imserver/{uid}")
@Component
public class WebsocketServer {
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,Session> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId="";

    @OnOpen
    public void onOpen(Session session,@PathParam("uid") String uid) {
        System.out.println("接收： "+ uid);
        webSocketMap.put(uid , session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println(message);
    }

    @OnClose
    public void onClose(Session session){
        System.out.println("Close!!");
    }

    public static void sendMsg(String uid ,String msg) throws IOException {
        webSocketMap.get(uid).getBasicRemote().sendText(msg);
    }
}