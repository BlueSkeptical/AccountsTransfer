package com.bnk.accounts.ws;

import com.bnk.accounts.*;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class WebTransferServiceTests {

    static final int PORT = 8080;
    static TransferServiceServer serviceServer;
    static Account account0;
    static Account account1;
    static Account account2;
    static TransferServiceServer webTransferServiceServer;
    static InetSocketAddress testServerAddress;
    
    @BeforeClass
    public static void setupEnvironment() throws Exception {
        account0 = new DefaultAccount(0, 100);
        account1 = new DefaultAccount(1, 200);
        account2 = new DefaultAccount(2, 200);
        
        new ServerStart().run();
        
        testServerAddress = new InetSocketAddress("localhost", PORT);
    }
    
    
    @Test
    public void should_correctly_transfer_some_amount_from_one_account_to_another() throws TransferException {
        final TransferService transferService = new HttpClientTransferService(testServerAddress);

        transferService.transfer(0, 1, 10);

        assertEquals(90, account0.balance());
        assertEquals(210, account1.balance());
    }
    
      static class ServerStart implements Runnable {

        @Override
        public void run() {
            serviceServer = new TransferServiceServer(PORT, new SimpleAccountsRepository(account0, account1, account2));
            try {
                serviceServer.start();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }  
    }
}
