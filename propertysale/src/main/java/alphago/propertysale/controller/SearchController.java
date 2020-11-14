package alphago.propertysale.controller;

import alphago.propertysale.entity.inVO.SearchModel;
import alphago.propertysale.service.AuctionService;
import alphago.propertysale.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: propertysale
 * @description: Controller for Search\
 **/

@RestController
public class SearchController {

    @Autowired
    private AuctionService service;

    /**
    * @Description: Search Ongoing auctions based on filter.
    * @Param:      model: Value of filter from Front-end
    * @return:    List of matched auctions
    */
    @RequestMapping("/search")
    Result getRunningOrComingAuctions(SearchModel model) {
        System.out.println(model);
        // if you have no query types....
        return Result.success(service.getSearchVO(model));
    }


}
