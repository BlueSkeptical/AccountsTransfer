package com.bnk.accounts.ws;

import com.bnk.accounts.TransferException;
import com.bnk.accounts.TransferService;

public class HttpClientTransferService implements TransferService {

    @Override
    public void transfer(int from, int to, long amount) throws TransferException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
