package com.bnk.accounts;

public class TransferException extends Exception {
    
    public TransferException() {
        super();
    }
    
    public TransferException(String reason) {
        super(reason);
    }
}
