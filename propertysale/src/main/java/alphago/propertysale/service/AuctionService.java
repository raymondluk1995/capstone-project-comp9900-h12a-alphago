package alphago.propertysale.service;

import alphago.propertysale.entity.Auction;
import alphago.propertysale.entity.returnVO.AuctionVO;
import alphago.propertysale.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AuctionService extends IService<Auction> {

    AuctionVO getAuctionByAid(long aid);

    AuctionVO getAuctionByProperty(long pid);
}
