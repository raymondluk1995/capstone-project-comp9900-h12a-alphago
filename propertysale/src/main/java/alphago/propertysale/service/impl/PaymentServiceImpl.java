package alphago.propertysale.service.impl;

import alphago.propertysale.entity.POJO.Payment;
import alphago.propertysale.mapper.PaymentMapper;
import alphago.propertysale.service.PaymentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-10-21
 */
@Service
@Transactional
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {
    @Autowired
    PaymentMapper paymentMapper;
}
