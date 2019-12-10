package sample.accounts;

/**
 * To be thrown then something goes wrong during transfer transaction
 */
@SuppressWarnings("serial")
public class TransferException extends RuntimeException {
    
    public TransferException() {
        super();
    }
    
    public TransferException(String reason) {
        super(reason);
    }
}
