package alphago.propertysale.utils;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-11-03 18:07
 **/
public class PriceUtil {
    public static int priceCompare(String p1, String p2){
        return Long.compare(Long.parseLong(p1), Long.parseLong(p2));
    }
}