package alphago.propertysale.exception;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-11-02 13:56
 **/
public class AuctionNotFoundException extends RuntimeException{
    public AuctionNotFoundException(String msg) {
        super(msg);
    }
}