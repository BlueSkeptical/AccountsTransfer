package com.bnk.accounts.ws;

import com.bnk.accounts.*;
import com.bnk.utils.Result;
import java.net.InetSocketAddress;
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
        
        account0 = new DefaultAccount(0, Value.ZERO, transferService);
        account1 = new DefaultAccount(1, Value.ZERO, transferService);
        account2 = new DefaultAccount(2, Value.ZERO, transferService);
        
        accountsRepository = new SimpleAccountsRepository(account0, account1, account2);
        
        synchronized(accountsRepository) {
            account0.deposit(new Value(100));
            account1.deposit(new Value(200));
            account2.deposit(new Value(300));
        }
         
        serviceServer = new TransferServiceServer(SERVER_PORT, "", accountsRepository, new DefaultTransferService());
        serviceServer.start(); 
    }
    
    @Test
    public void should_correctly_perform_transfers_routine() throws TransferException, NotAuhtorizedException
    {
        account0.transferTo(account1, new Value(10));
        synchronized(accountsRepository) {
            assertEquals(new Result.Success<>(new Value(90)), account0.balance());
            assertEquals(new Result.Success<>(new Value(210)), account1.balance());
            assertEquals(new Result.Success<>(new Value(300)), account2.balance());
        }
        
        account1.transferTo(account2, new Value(10));
        synchronized(accountsRepository) {
            assertEquals(new Result.Success<>(new Value(90)), account0.balance());
            assertEquals(new Result.Success<>(new Value(200)), account1.balance());
            assertEquals(new Result.Success<>(new Value(310)), account2.balance());
        }
        
        //an attempt to transfer more than left on the account
        assertEquals(new Result.Fail<>(new TransferException()),
                     account0.transferTo(account2, new Value(100)));
   
        synchronized(accountsRepository) {
            assertEquals(new Result.Success<>(new Value(90)), account0.balance());
            assertEquals(new Result.Success<>(new Value(200)), account1.balance());
            assertEquals(new Result.Success<>(new Value(310)), account2.balance());
        }
    }
}
