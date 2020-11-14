package alphago.propertysale.service;

import alphago.propertysale.entity.POJO.Rab;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
/**
 * @Description: Interface of service in Rab Controller
 */
public interface RabService extends IService<Rab> {

    void rabRegister(Rab rab);

    boolean isRegistered(long uid, long aid);

    List<Rab> getRunningAuctions(long uid);

    List<Rab> getPastAuctions(long uid);
}
