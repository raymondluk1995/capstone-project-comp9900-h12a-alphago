package alphago.propertysale.service;

import alphago.propertysale.entity.POJO.Rab;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-10-20
 */
public interface RabService extends IService<Rab> {

    void rabRegister(Rab rab);

    boolean isRegistered(long uid, long aid);

    List<Rab> getRunningAuctions(long uid);

    List<Rab> getPastAuctions(long uid);
}
