package alphago.propertysale.exception;

/**
 * @program: propertysale
 * @description: Exception if Auction has finished
 **/
public class AuctionFinishedException extends RuntimeException{
    public AuctionFinishedException(String message) {
        super(message);
    }
}