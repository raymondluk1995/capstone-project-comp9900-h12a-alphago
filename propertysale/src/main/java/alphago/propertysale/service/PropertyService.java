package alphago.propertysale.service;

import alphago.propertysale.entity.Address;
import alphago.propertysale.entity.Auction;
import alphago.propertysale.entity.Property;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PropertyService extends IService<Property> {
    void propertyRegister(long uid, Property property, Address address,
                          MultipartFile[] photos, Auction auction) throws IOException, InterruptedException;

    void propertyNewAuction(Auction auction, long uid);
}
