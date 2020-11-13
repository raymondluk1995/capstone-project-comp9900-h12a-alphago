package alphago.propertysale.mapper;

import alphago.propertysale.entity.POJO.Payment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-10-21
 */
public interface PaymentMapper extends BaseMapper<Payment> {

    @Select("select * from payment where card_number = #{cardNumber}")
    Payment getPaymentByCardNumber(String cardNumber);

}
