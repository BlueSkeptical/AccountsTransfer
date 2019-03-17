package com.bnk.accounts.ws;

import com.bnk.accounts.*;
import java.net.InetSocketAddress;
import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class WebTransferServiceTests {

    static final int SERVER_PORT = 8080;
    static TransferServiceServer serviceServer;
    static Account account0;
    static Account account1;
    static Account account2;
    static AccountsRepository accountsRepository;
    static TransferServiceServer webTransferServiceServer;
    static TransferService transferService;
    
    @BeforeClass
    public static void setupEnvironment() throws Exception {   
        transferService = new HttpClientTransferService(new InetSocketAddress("localhost", SERVER_PORT), "" );
        
        account0 = Account.newInstance(new AccountNumber(0),new OwnerName("Joe", "Doe"));
        account1 = Account.newInstance(new AccountNumber(1), new OwnerName("Mary", "Smith"));
        account2 = Account.newInstance(new AccountNumber(2), new OwnerName("Jan", "Kowalski"));
        
        final Order someInitialMoneyOrder0 = new DefaultOrder(account0.number(), new Value(100));
        final Order someInitialMoneyOrder1 = new DefaultOrder(account1.number(), new Value(200));
        accountsRepository = new SimpleAccountsRepository(Arrays.asList(someInitialMoneyOrder0, someInitialMoneyOrder1), account0, account1,account2);
        
        
        serviceServer = new TransferServiceServer(SERVER_PORT, "", accountsRepository, new DefaultTransferService(accountsRepository));
        serviceServer.start(); 
    }
    
    @Test
    public void should_correctly_perform_transfers_routine()
    {
        final Value transfered = transferService.transfer(account0.number(), account1.number(), new Value(10));
        assertEquals(new Value(10), transfered);
        assertEquals(new Value(90), accountsRepository.account(account0.number()).balance());
        assertEquals(new Value(210), accountsRepository.account(account1.number()).balance());
        assertEquals(new Value(0), accountsRepository.account(account2.number()).balance());
        
        /*
        transferService.transfer(account1, account2.number(), new Value(10));
        synchronized(accountsRepository) {
            assertEquals(new Value(90), account0.balance());
            assertEquals(new Value(200), account1.balance());
            assertEquals(new Value(310), account2.balance());
        }
        
        //an attempt to transfer more than left on the account
      //  assertEquals(new Result.Fail<>(new TransferException()),
      //               transferService.transfer(account0, account2.number(), new Value(100)));
   
        synchronized(accountsRepository) {
            assertEquals(new Value(90), account0.balance());
            assertEquals(new Value(200), account1.balance());
            assertEquals(new Value(310), account2.balance());
        } */
    }
}
