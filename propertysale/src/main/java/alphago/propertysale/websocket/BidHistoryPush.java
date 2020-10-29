package alphago.propertysale.websocket;

import alphago.propertysale.entity.RabAction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
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
    private static ConcurrentHashMap<String , List<RabAction>> history = new ConcurrentHashMap<>();
    private static ObjectMapper jsonMapper = new ObjectMapper();
    private String auctionId;

    @OnOpen
    public void onOpen(Session session , @PathParam(value = "aid") String aid) throws IOException {
        auctionId = aid;
        ConcurrentSkipListSet<Session> set = null;
        if(!map.containsKey(aid))
            map.put(aid , new ConcurrentSkipListSet<>(Comparator.comparing(Session::getId)));
        set = map.get(aid);
        set.add(session);
        // send history
        String msg = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(history.get(aid));
        session.getBasicRemote().sendText(msg);
    }

    @OnClose
    public void onClose(Session session){
        map.get(auctionId).remove(session);
    }

    public static void bidPush(String aid , RabAction bid){
        map.get(aid).forEach(session -> {
            try {
                history.get(aid).add(bid);
                String msg = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bid);
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}