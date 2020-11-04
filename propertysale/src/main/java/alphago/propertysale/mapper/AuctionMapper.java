package alphago.propertysale.mapper;

import alphago.propertysale.entity.Auction;
import alphago.propertysale.entity.RabAction;
import alphago.propertysale.entity.inVO.SearchModel;
import alphago.propertysale.entity.returnVO.SearchResVO;
import alphago.propertysale.entity.returnVO.SearchVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AuctionMapper extends BaseMapper<Auction> {

    @Select("SELECT aid, status FROM auction WHERE (status = 'R' OR status = 'A') AND pid = #{pid}")
    Auction getRunningAuctionById(long pid);

    @Select("SELECT * from rab_action r, auction a WHERE a.aid = #{aid} AND a.current_bid = r.rab_id")
    RabAction getAuctionCurrentBid(long aid);

    @Select("SELECT * FROM auction WHERE (status = 'R' OR status = 'A')")
    List<Auction> getAllRunningOrComingAuction();

    IPage<SearchVO> getSearchResults(SearchModel model);
}
