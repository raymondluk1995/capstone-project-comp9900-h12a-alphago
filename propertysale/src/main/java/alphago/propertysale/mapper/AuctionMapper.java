package alphago.propertysale.mapper;

import alphago.propertysale.entity.Auction;
import alphago.propertysale.entity.RabAction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

public interface AuctionMapper extends BaseMapper<Auction> {

    @Select("SELECT aid, status FROM auction WHERE (status = 'R' OR status = 'A') AND pid = #{pid}")
    Auction getRunningAuctionById(long pid);

    @Select("SELECT * from rab_action r, auction a WHERE a.aid = #{aid} AND a.current_bid = r.rab_id")
    RabAction getAuctionCurrentBid(long aid);
}
