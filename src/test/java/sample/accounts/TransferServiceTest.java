package sample.accounts;

import sample.accounts.DefaultTransferService;
import sample.accounts.Order;
import sample.accounts.AccountNumber;
import sample.accounts.OwnerName;
import sample.accounts.SimpleAccountsRepository;
import sample.accounts.Value;
import sample.accounts.AccountsRepository;
import sample.accounts.TransferService;
import sample.accounts.DefaultOrder;
import sample.accounts.Account;
import static lajkonik.fp.test.Assertions.require;
import java.util.Arrays;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;
import lajkonik.fp.*;

public class TransferServiceTest {

    private static Context createContext() {
        final Account acc0 = new DefaultAccount(new AccountNumber(0), new OwnerName("Joe", "Doe"));
        final Account acc1 = new DefaultAccount(new AccountNumber(1), new OwnerName("Mary", "Smith"));
        final Order someInitialMoneyOrder0 = new DefaultOrder(acc0.number(), new Value(100));
        final Order someInitialMoneyOrder1 = new DefaultOrder(acc1.number(), new Value(200));
        final AccountsRepository ar = new SimpleAccountsRepository(Arrays.asList(acc0, acc1), Arrays.asList(someInitialMoneyOrder0, someInitialMoneyOrder1));
        final TransferService ts = new DefaultTransferService(ar);
        return new Context(acc0, acc1, ar, ts);
    }
    
    @Test
    public void should_return_correct_tid_when_successfully_transfered_form_one_account_to_another() {
        final Context ctx = createContext();
     
        Assert.assertEquals(1, (int)ctx.ts.transfer(ctx.acc0.number(), ctx.acc1.number(), Value.of(10))
                  .run().getElseThrow(new RuntimeException()));
    }
    
    @Test
    public void should_correctly_transfer_some_amount_from_one_account_to_another() {
        final Context ctx = createContext();
     
        final Try<Integer> result = ctx.ts.transfer(ctx.acc0.number(), ctx.acc1.number(), Value.of(10))
                .run();
        result.onResult(r -> IO.effect(() -> { require(ctx.ar.account(ctx.acc0.number()).run(), v -> assertEquals(new Value(90), v.balance()));
                                               require(ctx.ar.account(ctx.acc1.number()).run(), v -> assertEquals(new Value(210), v.balance())); }),
                        e -> {throw new RuntimeException(e);} );
    }

    @Test
    public void should_keep_old_balance_after_exception_when_amount_on_source_account_is_not_enough() {
        final Context ctx = createContext();

        final Try<Integer> result = ctx.ts.transfer(ctx.acc0.number(), ctx.acc1.number(), Value.of(110)).run();
        result.onResult(r -> IO.effect(() -> { Assert.fail(); }),
                        e -> IO.effect(() -> { Assert.assertTrue(e instanceof TransferException);
                                               require(ctx.ar.account(ctx.acc0.number()).run(), v -> assertEquals(new Value(100), v.balance()));
                                               require(ctx.ar.account(ctx.acc1.number()).run(), v -> assertEquals(new Value(200), v.balance()));} ));
    }
    
    private static class Context {
        public final Account acc0;
        public final Account acc1;
        public final AccountsRepository ar;
        public final TransferService ts;

        public Context( Account acc0, Account acc1, AccountsRepository ar, TransferService ts) {
            this.acc0 = acc0;
            this.acc1 = acc1;
            this.ar = ar;
            this.ts = ts;
        }
    }
}
