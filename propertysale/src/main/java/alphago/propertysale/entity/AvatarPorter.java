package alphago.propertysale.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @program: propertysale
 * @description: This class is used to wrap a avatar that can be transferred in Message Queue
 **/
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AvatarPorter {
    private byte[] avatar;
    private String name;
    private long uid;
}