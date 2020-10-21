package alphago.propertysale.service.impl;

import alphago.propertysale.entity.*;
import alphago.propertysale.entity.returnVO.AuctionVO;
import alphago.propertysale.mapper.*;
import alphago.propertysale.service.AuctionService;
import alphago.propertysale.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-16 16:49
 **/
@Service
@Transactional
public class AuctionServiceImpl extends ServiceImpl<AuctionMapper , Auction> implements AuctionService {
    @Autowired
    AuctionMapper auctionMapper;
    @Autowired
    PropertyMapper propertyMapper;
    @Autowired
    AddressMapper addressMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RabMapper rabMapper;

    @Override
    public AuctionVO getAuctionByAid(long aid) {
        Auction auction = auctionMapper.selectById(aid);
        Property property = propertyMapper.selectById(auction.getPid());
        Address address = addressMapper.selectById(property.getPid());
        User user = userMapper.selectById(property.getOwner());
        AuctionVO auctionVO = new AuctionVO();
        BeanUtils.copyProperties(auction , auctionVO);
        BeanUtils.copyProperties(property , auctionVO);
        BeanUtils.copyProperties(user , auctionVO);
        BeanUtils.copyProperties(address , auctionVO , "address");
        auctionVO.setAvatar(FileUtil.getUserAvatar(user));
        auctionVO.setPhotos(FileUtil.getImages(auction.getPid()));
        auctionVO.setAddress(address.getFullAddress());
        // get current bid price
        if(auction.getCurrentBid() != 0){
            long bid = auction.getCurrentBid();
            // get highest price
            Rab rab = rabMapper.selectById(auction.getCurrentBid());
            auctionVO.setLatestPrice(rab.getHighestPrice());
        }else{
            auctionVO.setLatestPrice(auction.getMinimumPrice());
        }
        auctionVO.setStartdate(auction.getStartdate().getTime());
        auctionVO.setEnddate(auction.getEnddate().getTime());
        return auctionVO;
    }

    @Override
    public AuctionVO getAuctionByProperty(long pid) {
        Auction auction = auctionMapper.selectOne(new QueryWrapper<Auction>()
                .eq("pid" , pid).eq("status" , "R"));
        return getAuctionByAid(auction.getAid());
    }
}