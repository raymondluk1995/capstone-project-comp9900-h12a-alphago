package alphago.propertysale.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-11-02 15:46
 **/
public class TimeUtil {

    public static ZoneOffset getMyZone(){
        ZoneId zoneId = ZoneId.systemDefault();
        return zoneId.getRules().getOffset(Instant.now());
    }
}