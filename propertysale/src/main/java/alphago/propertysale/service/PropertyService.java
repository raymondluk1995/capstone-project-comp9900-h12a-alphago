package alphago.propertysale.service;

import alphago.propertysale.entity.POJO.Address;
import alphago.propertysale.entity.POJO.Auction;
import alphago.propertysale.entity.POJO.Property;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PropertyService extends IService<Property> {
    void propertyRegister(long uid, Property property, Address address,
                          MultipartFile[] photos, Auction auction) throws IOException, InterruptedException;

    void propertyNewAuction(Auction auction, long uid);
}
