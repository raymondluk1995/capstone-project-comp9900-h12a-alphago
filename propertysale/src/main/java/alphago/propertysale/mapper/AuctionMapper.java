package alphago.propertysale.mapper;

import alphago.propertysale.entity.Auction;
import alphago.propertysale.entity.RabAction;
import alphago.propertysale.entity.inVO.SearchModel;
import alphago.propertysale.entity.returnVO.RunningAuctionAddress;
import alphago.propertysale.entity.returnVO.SearchResVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AuctionMapper extends BaseMapper<Auction> {

    @Select("SELECT aid, status FROM auction WHERE (status = 'R' OR status = 'A') AND pid = #{pid}")
    Auction getRunningAuctionById(long pid);

    @Select("SELECT * from rab_action r, auction a WHERE a.aid = #{aid} AND a.current_bid = r.rab_id")
    RabAction getAuctionCurrentBid(long aid);

    @Select("SELECT * FROM auction WHERE (status = 'R' OR status = 'A')")
    List<Auction> getAllRunningOrComingAuction();

    @Select("SELECT au.aid, p.pid, p.bathroom_num , p.bedroom_num , p.garage_num , ad.lat , ad.lng , ad.address ,ad.state ,ad.postcode,ad.suburb \n" +
            "FROM auction au , property p , address ad \n" +
            "WHERE (au.status = 'A' OR au.status = 'R') \n" +
            "AND au.pid = p.pid \n" +
            "AND p.pid = ad.pid ;")
    List<RunningAuctionAddress> getRunningAuctionAddress();
    @Select("SELECT auc.pid, auc.aid, auc.status, auc.current_bid, auc.start_date, auc.end_date, auc.bidder_num," +
            "prop.bathroom_num, prop.bedroom_num, prop.garage_num, prop.area, prop.type  FROM address a, auction auc, property prop ${ew.customSqlSegment}  AND (auc.pid = a.pid)" +
            "and (auc.pid = prop.pid)  and (auc.status = 'R' or auc.status = 'A') ")
    IPage<SearchResVO> getAllRunningOrComingRes(IPage<SearchResVO> page, @Param(Constants.WRAPPER) Wrapper<SearchResVO> wrapper);

}
