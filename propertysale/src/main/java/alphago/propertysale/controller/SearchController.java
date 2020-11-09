package alphago.propertysale.controller;

import alphago.propertysale.entity.inVO.SearchModel;
import alphago.propertysale.service.AuctionService;
import alphago.propertysale.utils.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: propertysale
 * @description: Controller for Search
 * @author: TAO XUE
 * @create: 2020-11-03 08:42
 **/

@RestController
public class SearchController {

    @Autowired
    private AuctionService service;

    @RequestMapping("/search")
    Result getRunningOrComingAuctions(SearchModel model) {
        System.out.println(model);
        // if you have no query types....
        return Result.success(service.getSearchVO(model));
    }


}
