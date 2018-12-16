package com.bnk.accounts;

/**
 * To be thrown then something goes wrong during transfer transaction
 */
public class TransferException extends Exception {
    
    public TransferException() {
        super();
    }
    
    public TransferException(String reason) {
        super(reason);
    }
}
