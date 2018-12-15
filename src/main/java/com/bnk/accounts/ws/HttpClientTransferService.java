package com.bnk.accounts.ws;

import com.bnk.accounts.TransferException;
import com.bnk.accounts.TransferService;
import java.net.InetSocketAddress;

public class HttpClientTransferService implements TransferService {


    HttpClientTransferService(InetSocketAddress testServerAddress) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void transfer(int from, int to, long amount) throws TransferException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
