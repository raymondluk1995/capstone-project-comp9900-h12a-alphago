package alphago.propertysale.entity.POJO;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-08 23:38
 **/
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AvatarPorter {
    private byte[] avatar;
    private String name;
    private long uid;
}