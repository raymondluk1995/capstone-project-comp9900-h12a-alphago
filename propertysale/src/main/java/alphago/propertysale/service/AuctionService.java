package alphago.propertysale.service;

import alphago.propertysale.entity.POJO.Auction;
import alphago.propertysale.entity.POJO.AuctionStatus;
import alphago.propertysale.entity.inVO.SearchModel;
import alphago.propertysale.entity.returnVO.AuctionVO;
import alphago.propertysale.entity.returnVO.SearchVO;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * @Description: Interface of service in Auction Controller
 */
public interface AuctionService extends IService<Auction> {

    AuctionVO getAuctionByAid(long aid);

    AuctionVO getAuctionByProperty(long pid);

    AuctionStatus getAuctionStatus(long pid);

    void auctionCancel(long pid, long aid);

    void initHistory(long aid);

    void finishAuction(long aid);

    SearchVO getSearchVO(SearchModel model);
}
