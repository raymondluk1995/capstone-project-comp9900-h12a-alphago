package alphago.propertysale.mapper;

import alphago.propertysale.entity.Rab;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-10-20
 */
public interface RabMapper extends BaseMapper<Rab> {

    @Select("SELECT * FROM rab r , auction a WHERE r.uid = #{uid} AND r.aid = a.aid AND (a.status = 'R' OR a.status = 'A')")
    List<Rab> getRunningAuctions(long uid);
    @Select("SELECT * FROM rab r , auction a WHERE r.uid = #{uid} AND r.aid = a.aid AND (a.status = 'R' OR a.status = 'A')")
    List<Rab> getPastAuctions(long uid);
}
