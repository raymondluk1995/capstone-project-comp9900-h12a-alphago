package alphago.propertysale.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-16 17:23
 **/
@Component
@ServerEndpoint(value = "/auction/{aid}", configurator = alphago.propertysale.config.WebsocketConfig.class)
public class BidHistoryPush {

    private static ConcurrentHashMap<String , ConcurrentSkipListSet<Session>> map = new ConcurrentHashMap<>();
    private String auctionId;

    @OnOpen
    public void onOpen(Session session , @PathParam(value = "aid") String aid){
        auctionId = aid;
        System.out.println(session.getId());
        ConcurrentSkipListSet<Session> set = null;
        if(!map.containsKey(aid))
            map.put(aid , new ConcurrentSkipListSet<>(Comparator.comparing(Session::getId)));
        set = map.get(aid);
        set.add(session);

        System.out.println(map);
    }

    @OnClose
    public void onClose(Session session){
        map.get(auctionId).remove(session);
        System.out.println(map);
    }

    public static void bidPush(String aid , String msg){
        map.get(aid).forEach(session -> {
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}