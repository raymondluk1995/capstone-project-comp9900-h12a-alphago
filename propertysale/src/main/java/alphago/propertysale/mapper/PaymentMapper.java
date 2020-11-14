package alphago.propertysale.mapper;

import alphago.propertysale.entity.POJO.Payment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: Data access layer for Payment table in database
 */

public interface PaymentMapper extends BaseMapper<Payment> {

    @Select("select * from payment where card_number = #{cardNumber}")
    Payment getPaymentByCardNumber(String cardNumber);

}
