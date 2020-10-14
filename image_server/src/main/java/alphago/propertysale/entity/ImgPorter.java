package alphago.propertysale.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-13 22:29
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