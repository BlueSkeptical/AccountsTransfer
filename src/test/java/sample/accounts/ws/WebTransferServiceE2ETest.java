package sample.accounts.ws;

import sample.accounts.ws.TransferServiceServer;
import sample.accounts.ws.HttpClientTransferService;
import sample.accounts.DefaultTransferService;
import sample.accounts.Order;
import sample.accounts.AccountNumber;
import sample.accounts.OwnerName;
import sample.accounts.SimpleAccountsRepository;
import sample.accounts.TransferException;
import sample.accounts.Value;
import sample.accounts.AccountsRepository;
import sample.accounts.DefaultOrder;
import sample.accounts.TransferService;
import sample.accounts.Account;
import static lajkonik.fp.test.Assertions.require;

import lajkonik.fp.IO;
import lajkonik.fp.Try;

import java.net.InetSocketAddress;
import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class WebTransferServiceE2ETest {

    static final int SERVER_PORT = 8080;
    static TransferServiceServer serviceServer;
    static Account acc0;
    static Account acc1;
    static AccountsRepository ar;
    static TransferServiceServer webTransferServiceServer;
    static TransferService ts;
    
    @BeforeClass
    public static void setupEnvironment() throws Exception {   
        ts = new HttpClientTransferService(new InetSocketAddress("localhost", SERVER_PORT));
        
        acc0 = Account.newInstance(new AccountNumber(0),new OwnerName("Joe", "Doe"));
        acc1 = Account.newInstance(new AccountNumber(1), new OwnerName("Mary", "Smith"));
        
        final Order initialMoneyOrder0 = new DefaultOrder(acc0.number(), new Value(100));
        final Order initialMoneyOrder1 = new DefaultOrder(acc1.number(), new Value(200));
        ar = new SimpleAccountsRepository(Arrays.asList(acc0, acc1), Arrays.asList(initialMoneyOrder0, initialMoneyOrder1));
        
        
        serviceServer = new TransferServiceServer(SERVER_PORT, new TransferServlet(new DefaultTransferService(ar)));
        serviceServer.start(); 
    }
    
    @Test
    public void should_increase_target_account_and_decrease_source_account_by_transfer_amount() {
        final Value oldValue0 = getBalance(acc0.number());
        final Value oldValue1 = getBalance(acc1.number());

        final Value amount = new Value(10);
        final Try<Integer> result = ts.transfer(acc0.number(), acc1.number(), amount).run();
        
        require(result, p -> assertTrue(p >= 1)); 
        
        require(ar.account(acc0.number()).run(), v -> assertEquals(oldValue0.substract(amount), v.balance()));
        require(ar.account(acc1.number()).run(), v -> assertEquals(oldValue1.add(amount), v.balance()));
    }
    
    
    @Test
    public void should_fail_and_keep_old_balances_when_amount_on_source_account_is_not_enough() {
        final Value oldValue0 = getBalance(acc0.number());
        final Value oldValue1 = getBalance(acc1.number());
        

        final Value tooMuchAmount = oldValue0.add(new Value(10));
        final Try<Integer> result = ts.transfer(acc0.number(), acc1.number(), tooMuchAmount).run();

        result.onResult(r -> IO.effect(() -> { fail(); }),
                        ex -> IO.effect(() -> { assertTrue(ex instanceof TransferException); } ));

        require(ar.account(acc0.number()).run(), v -> assertEquals(oldValue0, v.balance()));
        require(ar.account(acc1.number()).run(), v -> assertEquals(oldValue1, v.balance()));
    }

    private static Value getBalance(AccountNumber accountNumber) {
        return ar.account(accountNumber).run().getElseThrow(new IllegalStateException()).balance();
    }
}
