package alphago.propertysale.service.impl;

import alphago.propertysale.entity.Address;
import alphago.propertysale.mapper.AddressMapper;
import alphago.propertysale.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-13 16:28
 **/
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper , Address> implements AddressService{
}