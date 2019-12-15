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

import lajkonik.fp.Try;

import java.net.InetSocketAddress;
import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/** 
 * An end-to-end test for the server and client
*/
public class WebTransferServiceE2ETest {

    private static final int SERVER_PORT = 8080;
    private static final AccountNumber accNumber0 = new AccountNumber(0);
    private static final AccountNumber accNumber1 = new AccountNumber(1);

    static TransferServiceServer serviceServer;
    static AccountsRepository ar;
    
    @BeforeClass
    public static void setupEnvironment() throws Exception {   
        final Account acc0 = Account.newInstance(accNumber0,new OwnerName("Joe", "Doe"));
        final Account acc1 = Account.newInstance(accNumber1, new OwnerName("Mary", "Smith"));
        
        final Order initialMoneyOrder0 = new DefaultOrder(accNumber0, new Value(100));
        final Order initialMoneyOrder1 = new DefaultOrder(accNumber1, new Value(200));
        ar = new SimpleAccountsRepository(Arrays.asList(acc0, acc1), Arrays.asList(initialMoneyOrder0, initialMoneyOrder1));
        
        serviceServer = new TransferServiceServer(SERVER_PORT, new TransferServlet(new DefaultTransferService(ar)));
        serviceServer.start(); 
    }
    
    @Test
    public void should_increase_target_account_and_decrease_source_account_by_transfer_amount() {
        final Value oldValue0 = getBalance(accNumber0);
        final Value oldValue1 = getBalance(accNumber1);

        final Value amount = new Value(10);
        final Try<Integer> result = createTransferService().transfer(accNumber0, accNumber1, amount).run();
        
        require(result, p -> assertTrue(p >= 1)); 
        
        require(ar.account(accNumber0).run(), v -> assertEquals(oldValue0.substract(amount), v.balance()));
        require(ar.account(accNumber1).run(), v -> assertEquals(oldValue1.add(amount), v.balance()));
    }
    
    @Test
    public void should_fail_and_keep_old_balances_when_amount_on_source_account_is_not_enough() {
        final Value oldValue0 = getBalance(accNumber0);
        final Value oldValue1 = getBalance(accNumber1);
        
        final Value tooMuchAmount = oldValue0.add(new Value(10));
        final Try<Integer> result = createTransferService().transfer(accNumber0, accNumber1, tooMuchAmount).run();

        result.onResult(r -> { fail(); },
                        ex  -> { assertTrue(ex instanceof TransferException); } );

        require(ar.account(accNumber0).run(), v -> assertEquals(oldValue0, v.balance()));
        require(ar.account(accNumber1).run(), v -> assertEquals(oldValue1, v.balance()));
    }

    @Test
    public void should_fail_on_wrong_web_service_endpoint() {
        final Value amount = new Value(10);
        final Try<Integer> result = createWrongServerPortTransferService().transfer(accNumber0, accNumber1, amount).run();

        result.onResult(r -> { fail(); },
                        ex  -> { assertTrue(ex instanceof RuntimeException);
                                 assertFalse(ex instanceof TransferException); } );
    }

    private static TransferService createTransferService() {
        return new HttpClientTransferService(new InetSocketAddress("localhost", SERVER_PORT));
    }

    private static TransferService createWrongServerPortTransferService() {
        return new HttpClientTransferService(new InetSocketAddress("localhost", SERVER_PORT+1));
    }

    private static Value getBalance(AccountNumber accountNumber) {
        return ar.account(accountNumber).run().getElseThrow(new IllegalStateException()).balance();
    }
}
