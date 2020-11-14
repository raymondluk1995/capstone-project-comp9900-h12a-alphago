package alphago.propertysale.service;

import alphago.propertysale.entity.POJO.RabAction;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: Interface of service in RabAction Controller
 */
public interface RabActionService extends IService<RabAction> {

    boolean bid(RabAction rabAction);
}
