package alphago.propertysale.service.impl;

import alphago.propertysale.entity.Auction;
import alphago.propertysale.mapper.AuctionMapper;
import alphago.propertysale.service.AuctionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-16 16:49
 **/
@Service
public class AuctionServiceImpl extends ServiceImpl<AuctionMapper , Auction> implements AuctionService {
}