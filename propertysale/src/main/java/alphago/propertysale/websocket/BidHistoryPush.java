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
 * @description: This class is the websocket for communication between the server and the client.
 *               Mainly used to push bid result to all clients visiting the auction page.
 *
 *               The ConcurrentHashMap stores All Session established between each auction page and server.
 **/
@Component
@ServerEndpoint(value = "/auction/{aid}", configurator = alphago.propertysale.config.WebsocketConfig.class)
public class BidHistoryPush {

    private static ConcurrentHashMap<String , ConcurrentSkipListSet<Session>> map = new ConcurrentHashMap<>();
    private static ObjectMapper jsonMapper = new ObjectMapper();
    private String auctionId;

    /**
    * @Description: Communication establish
     *              Add aid and the session into the map
    */
    @OnOpen
    public void onOpen(Session session , @PathParam(value = "aid") String aid) throws IOException {
        auctionId = aid;
        if(!map.containsKey(aid))
            map.put(aid , new ConcurrentSkipListSet<>(Comparator.comparing(Session::getId)));
        map.get(aid).add(session);

        System.out.println(map);
    }

    /**
     * @Description: Communication close
     *               Delete the session
     */
    @OnClose
    public void onClose(Session session){
        map.get(auctionId).remove(session);
    }

    /**
    * @Description: Once a new bid come, push the message to all sessions in map.
    * @Param: aid: auction aid
     *        bidMsg: bid message
    */
    public static void bidPush(long aid , BidMsg bidMsg) {
        try {
            if(map.get(String.valueOf(aid)) == null) return;
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

    /**
    * @Description: Inform auction page to refresh
    */
    public static void refresh(long aid){
        System.out.println("refresh " + map.get(String.valueOf(aid)));
        if(map.get(String.valueOf(aid)) == null) return;
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

    /**
     * @Description: Inform auction page to a new bidder come in
     */
    public static void newBidder(long aid, Long price){
        try {
            HashMap<String, Object> m = new HashMap<>();
            m.put("newBidder", true);
            m.put("username", null);
            m.put("price", price);
            String msg = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(m);
            if(map.get(String.valueOf(aid)) == null) return;
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

    /**
    * @Description: Add biding history in redis
    */
    public static void initHistory(long aid){
        RedisTemplate redis = RedisUtil.getRedis();
        redis.opsForValue().set("History:"+aid, new ArrayList<BidMsg>());
    }

    /**
     * @Description: Get biding history from redis
     */
    public static List<BidMsg> getAuctionHistory(long aid){
        RedisTemplate redis = RedisUtil.getRedis();
        Object history = redis.opsForValue().get("History:" + aid);
        return history == null ? new ArrayList<>() : (List<BidMsg>)history;
    }

    /**
     * @Description: Add biding message in redis
     */
    public static void addBidHistory(long aid, BidMsg bidMsg){
        List<BidMsg> auctionHistory = getAuctionHistory(aid);
        auctionHistory.add(bidMsg);
        RedisTemplate redis = RedisUtil.getRedis();
        redis.opsForValue().set("History:"+aid, auctionHistory);
    }

    /**
    * @Description: Remove history
    */
    public static List<BidMsg> removeAuctionHistory(long aid){
        RedisTemplate redis = RedisUtil.getRedis();
        List<BidMsg> auctionHistory = getAuctionHistory(aid);
        redis.delete("History:"+aid);
        return auctionHistory;
    }
}