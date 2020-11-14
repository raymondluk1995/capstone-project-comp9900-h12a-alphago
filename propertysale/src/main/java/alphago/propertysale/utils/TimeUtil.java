package alphago.propertysale.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * @program: propertysale
 * @description: Get the system's timezone offset
 **/
public class TimeUtil {

    public static ZoneOffset getMyZone(){
        ZoneId zoneId = ZoneId.systemDefault();
        return zoneId.getRules().getOffset(Instant.now());
    }
}