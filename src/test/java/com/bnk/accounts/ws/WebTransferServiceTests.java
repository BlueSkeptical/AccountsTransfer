package com.bnk.accounts.ws;

import com.bnk.accounts.*;
import static com.bnk.accounts.TransferServiceTests.require;
import com.bnk.utils.fp.IO;
import com.bnk.utils.fp.Try;
import java.net.InetSocketAddress;
import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class WebTransferServiceTests {

    static final int SERVER_PORT = 8080;
    static TransferServiceServer serviceServer;
    static Account acc0;
    static Account acc1;
    static AccountsRepository ar;
    static TransferServiceServer webTransferServiceServer;
    static TransferService ts;
    
    @BeforeClass
    public static void setupEnvironment() throws Exception {   
        ts = new HttpClientTransferService(new InetSocketAddress("localhost", SERVER_PORT), "" );
        
        acc0 = Account.newInstance(new AccountNumber(0),new OwnerName("Joe", "Doe"));
        acc1 = Account.newInstance(new AccountNumber(1), new OwnerName("Mary", "Smith"));
        
        final Order someInitialMoneyOrder0 = new DefaultOrder(acc0.number(), new Value(100));
        final Order someInitialMoneyOrder1 = new DefaultOrder(acc1.number(), new Value(200));
        ar = new SimpleAccountsRepository(Arrays.asList(someInitialMoneyOrder0, someInitialMoneyOrder1), acc0, acc1);
        
        
        serviceServer = new TransferServiceServer(SERVER_PORT, "", ar, new DefaultTransferService(ar));
        serviceServer.start(); 
    }
    
    @Test
    public void should_correctly_perform_transfers_routine()
    {
        final IO<Integer> result = ts.transfer(acc0.number(), acc1.number(), new Value(10));
        
        require(result, p -> assertTrue( p >= 1)); 
        
        require(ar.account(acc0.number()), v -> assertEquals(new Value(90), v.balance()));
        require(ar.account(acc1.number()), v -> assertEquals(new Value(210), v.balance()));
    }
    
    
    //@Test
    public void should_keep_old_balance_after_exception_when_amount_on_source_account_is_not_enough() {
/*
        final Value oldValue0 = ar.account(acc0.number()).balance();
        final Value oldValue1 = ar.account(acc1.number()).balance();
        
        final Try<Integer> result = ts.transfer(acc0.number(), acc1.number(), new Value(110)).run();
        
        result.io(v -> IO.effect(() -> fail("Unexpected value")),
                  ex ->  IO.effect(() ->{ assertEquals(oldValue0, ar.account(acc0.number()).balance());
                                         assertEquals(oldValue1, ar.account(acc1.number()).balance()); })).run();
*/
    }
}
