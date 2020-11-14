package alphago.propertysale.service.impl;

import alphago.propertysale.entity.POJO.Address;
import alphago.propertysale.mapper.AddressMapper;
import alphago.propertysale.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @program: propertysale
 * @description: Implementation of Address service
 **/
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper , Address> implements AddressService{
}