package alphago.propertysale.service.impl;

import alphago.propertysale.entity.POJO.Address;
import alphago.propertysale.entity.POJO.Auction;
import alphago.propertysale.entity.ImgPorter;
import alphago.propertysale.entity.POJO.Property;
import alphago.propertysale.mapper.AddressMapper;
import alphago.propertysale.mapper.AuctionMapper;
import alphago.propertysale.mapper.PropertyMapper;
import alphago.propertysale.mapper.RabMapper;
import alphago.propertysale.rabbit.MessageProducer;
import alphago.propertysale.service.PropertyService;
import alphago.propertysale.utils.CheckCode;
import alphago.propertysale.utils.RedisUtil;
import alphago.propertysale.utils.TimeUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @program: propertysale
 * @description: Implementation of Property service
 **/
@Service
@Transactional
public class PropertyServiceImpl extends ServiceImpl<PropertyMapper , Property> implements PropertyService {
    @Autowired
    private PropertyMapper propertyMapper;

    @Autowired
    private AuctionMapper auctionMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private RabMapper rabMapper;

    public void propertyRegister(long uid, Property property, Address address,
                                 MultipartFile[] photos, Auction auction) throws IOException, InterruptedException {
        // Register Property
        property.setOwner(uid);
        propertyMapper.insert(property);
        // Save Address
        Long pid = property.getPid();
        address.setPid(pid);
        addressMapper.insert(address);

        // upload file
        ImgPorter[] imgPorters = new ImgPorter[photos.length];
        for(int i = 0 ; i < photos.length ; i++){
            imgPorters[i] = new ImgPorter().setImage(photos[i].getBytes())
                    .setName(photos[i].getOriginalFilename())
                    .setPid(pid).setUid(uid);
        }
        messageProducer.sendMsg(imgPorters , CheckCode.IMAGE);
        // Register Auction
        if(property.isAuction()){
            auction.setPid(pid);
            registerNewAuction(auction, property.getOwner());
        }
    }

    /**
    * @Description: Register new auction
    */
    @Override
    public void propertyNewAuction(Auction auction, long seller) {
        propertyMapper.update(null, new UpdateWrapper<Property>()
                .eq("pid", auction.getPid()).set("auction", true));
        registerNewAuction(auction, seller);
    }

    /**
     * @Description: Register new auction
     */
    private void registerNewAuction(Auction auction, long seller){
        System.out.println(auction.getStartdate().toInstant(TimeUtil.getMyZone()).toEpochMilli() -
                System.currentTimeMillis());

        auction.setStatus(Auction.REGISTERED);
        auction.setSeller(seller);
        auction.setHighestPrice(auction.getMinimumPrice());
        auctionMapper.insert(auction);
        // Set count down
        RedisTemplate redisTemplate = RedisUtil.valueRedis();
        redisTemplate.opsForValue().set("Start:" + auction.getAid() , ""
                , auction.getStartdate().toInstant(TimeUtil.getMyZone()).toEpochMilli() -
                        System.currentTimeMillis() , TimeUnit.MILLISECONDS);
    }
}