package alphago.propertysale.exception;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-11-04 18:58
 **/
public class AuctionFinishedException extends RuntimeException{
    public AuctionFinishedException(String message) {
        super(message);
    }
}