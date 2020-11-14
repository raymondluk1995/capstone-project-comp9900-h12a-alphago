package alphago.propertysale.entity.POJO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @program: propertysale
 * @description: Store Property's image in the binary form along with its name and the uid of its owner.
 **/
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ImgPorter {
    private byte[] image;
    private String name;
    private long uid;
    private long pid;
}