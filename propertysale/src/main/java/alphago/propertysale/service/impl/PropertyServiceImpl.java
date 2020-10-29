package alphago.propertysale.service.impl;

import alphago.propertysale.entity.Property;
import alphago.propertysale.mapper.PropertyMapper;
import alphago.propertysale.service.PropertyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-13 15:30
 **/
@Service
@Transactional
public class PropertyServiceImpl extends ServiceImpl<PropertyMapper , Property> implements PropertyService {
}