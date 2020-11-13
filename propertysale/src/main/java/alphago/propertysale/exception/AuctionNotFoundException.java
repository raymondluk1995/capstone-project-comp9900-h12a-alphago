package alphago.propertysale.exception;

/**
 * @program: propertysale
 * @description: Exception if auction can not be found
 **/
public class AuctionNotFoundException extends RuntimeException{
    public AuctionNotFoundException(String msg) {
        super(msg);
    }
}