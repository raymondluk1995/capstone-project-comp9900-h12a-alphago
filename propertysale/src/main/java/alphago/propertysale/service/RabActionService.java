package alphago.propertysale.service;

import alphago.propertysale.entity.RabAction;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-10-24
 */
public interface RabActionService extends IService<RabAction> {

    boolean bid(RabAction rabAction);
}
