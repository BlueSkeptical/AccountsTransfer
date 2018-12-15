package com.bnk.accounts.ws;

import com.bnk.accounts.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class WebTransferServiceTests {

    static final int PORT = 8080;
    
    Account account0;
    Account account1;
    TransferServiceServer webTransferServiceServer;
    InetSocketAddress testServerAddress;
    
    
    
    @BeforeClass
    public void setupEnvironment() {
        account0 = new DefaultAccount(0, 100);
        account1 = new DefaultAccount(1, 200);
        
        TransferServiceServer serviceServer = new TransferServiceServer(PORT, new SimpleAccountsRepository(account0, account1));
        serviceServer.start();
        
        testServerAddress = new InetSocketAddress("localhost", PORT);
    }
    
    
    @Test
    public void should_correctly_transfer_some_amount_from_one_account_to_another() throws TransferException {
        final TransferService transferService = new HttpClientTransferService(testServerAddress);

        transferService.transfer(0, 1, 10);

        assertEquals(90, account0.balance());
        assertEquals(210, account1.balance());
    }
    
    @Test(expected = TransferException.class)
    public void should_throw_exception_when_amount_on_source_account_is_not_enough() throws TransferException {
        final Account account0 = new DefaultAccount(0, 100);
        final Account account1 = new DefaultAccount(1, 200);
        final TransferService transferService = new HttpClientTransferService(testServerAddress);
      
        transferService.transfer(0, 1, 101);
    }
    
    @Test
    public void should_keep_old_balance_after_exception_when_amount_on_source_account_is_not_enough() {
        final Account account0 = new DefaultAccount(0, 100);
        final Account account1 = new DefaultAccount(1, 200);
        final TransferService transferService = new HttpClientTransferService(testServerAddress);
        
        try {
            transferService.transfer(0, 1, 101);
        } catch (TransferException ex) {
            //NO-OP
        }

        assertEquals(100, account0.balance());
        assertEquals(200, account1.balance());
    }
   
}
