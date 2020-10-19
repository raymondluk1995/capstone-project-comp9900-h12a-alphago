package alphago.propertysale.controller;

import alphago.propertysale.websocket.BidHistoryPush;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-17 13:18
 **/
@RestController
@RequestMapping("/bid")
public class BidController {
    @RequestMapping("/{aid}")
    public void bid(@PathVariable String aid){
        BidHistoryPush.bidPush(aid , "User just bid 10000000$");
    }
}