package alphago.propertysale.websocket;

import alphago.propertysale.utils.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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
    private static ObjectMapper jsonMapper = new ObjectMapper();
    private String auctionId;

    @OnOpen
    public void onOpen(Session session , @PathParam(value = "aid") String aid) throws IOException {
        auctionId = aid;
        if(!map.containsKey(aid))
            map.put(aid , new ConcurrentSkipListSet<>(Comparator.comparing(Session::getId)));
        map.get(aid).add(session);

        System.out.println(map);
    }

    @OnClose
    public void onClose(Session session){
        map.get(auctionId).remove(session);
    }

    public static void bidPush(long aid , BidMsg bidMsg) {
        try {
            String msg = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bidMsg);
            map.get(String.valueOf(aid)).forEach(session -> {
                try {
                    session.getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        addBidHistory(aid, bidMsg);
    }

    public static void refresh(long aid){
        try {
            HashMap<String, Boolean> m = new HashMap<>();
            m.put("refresh", true);
            String msg = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(m);
            map.get(String.valueOf(aid)).forEach(session -> {
                        try {
                            session.getBasicRemote().sendText(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void initHistory(long aid){
        RedisTemplate redis = RedisUtil.getRedis();
        redis.opsForValue().set("History:"+aid, new ArrayList<BidMsg>());
    }

    public static List<BidMsg> getAuctionHistory(long aid){
        RedisTemplate redis = RedisUtil.getRedis();
        Object history = redis.opsForValue().get("History:" + aid);
        return history == null ? new ArrayList<>() : (List<BidMsg>)history;
    }

    public static void addBidHistory(long aid, BidMsg bidMsg){
        List<BidMsg> auctionHistory = getAuctionHistory(aid);
        auctionHistory.add(bidMsg);
        RedisTemplate redis = RedisUtil.getRedis();
        redis.opsForValue().set("History:"+aid, auctionHistory);
    }

    public static List<BidMsg> removeAuctionHistory(long aid){
        RedisTemplate redis = RedisUtil.getRedis();
        List<BidMsg> auctionHistory = getAuctionHistory(aid);
        redis.delete("History:"+aid);
        return auctionHistory;
    }
}