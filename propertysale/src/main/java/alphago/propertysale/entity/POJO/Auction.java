package alphago.propertysale.entity.POJO;

import alphago.propertysale.utils.RedisUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @program: propertysale
 * @description: Auction Entity
 * @author: XIAO HAN
 * @create: 2020-10-15 15:35
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Auction {
    public final static String REGISTERED = "R";
    public final static String AUCTION = "A";
    public final static String SUCCESS = "S";
    public final static String FAIL = "F";

    @TableId(type = IdType.AUTO)
    private long aid;
    private long pid;
    private long seller;
    @TableField(value = "start_date")
    private LocalDateTime startdate;
    @TableField(value = "end_date")
    private LocalDateTime enddate;
    @TableField(value = "reserve_price")
    private Long price;
    private Long minimumPrice;
    private Long highestPrice;
    private long currentBid;
    private String status;
    private long bidderNum = 0;

    public static boolean isExpr(long aid){
        RedisTemplate redis = RedisUtil.valueRedis();
        String key = "End:"+aid;
        long expr = redis.getExpire(key, TimeUnit.MILLISECONDS);
        System.out.println(expr);
        if(expr < 5 * 60 * 1000){
            System.out.println("Before: redis " + redis.getExpire(key, TimeUnit.MILLISECONDS));
            redis.expire(key, expr + 2 * 60 * 1000 , TimeUnit.MILLISECONDS);
            System.out.println("Now: redis " + redis.getExpire(key, TimeUnit.MILLISECONDS));
            return true;
        }else{
            return false;
        }
    }

    public boolean isFinish(){
        return status.equals(SUCCESS) || status.equals(FAIL);
    }
}