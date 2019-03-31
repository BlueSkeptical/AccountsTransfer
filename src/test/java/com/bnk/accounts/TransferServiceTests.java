package com.bnk.accounts;

import static com.bnk.utils.fp.Assertions.require;
import com.bnk.utils.fp.IO;
import java.util.Arrays;
import static org.junit.Assert.*;
import com.bnk.utils.fp.Nothing;
import java.util.function.Consumer;
import org.junit.Assert;
import org.junit.AssumptionViolatedException;
import org.junit.Test;

public class TransferServiceTests {

    private static Context createContext() {
        final Account acc0 = Account.newInstance(new AccountNumber(0), new OwnerName("Joe", "Doe"));
        final Account acc1 = Account.newInstance(new AccountNumber(1), new OwnerName("Mary", "Smith"));
        final Order someInitialMoneyOrder0 = new DefaultOrder(acc0.number(), new Value(100));
        final Order someInitialMoneyOrder1 = new DefaultOrder(acc1.number(), new Value(200));
        final AccountsRepository ar = new SimpleAccountsRepository(Arrays.asList(someInitialMoneyOrder0, someInitialMoneyOrder1), acc0, acc1);
        final TransferService ts = new DefaultTransferService(ar);
        return new Context(acc0, acc1, ar, ts);
    }
    
    @Test
    public void should_return_correct_tid_when_successfully_transfered_form_one_account_to_another() {
        final Context ctx = createContext();
     
        final IO<Integer> value = ctx.ts.transfer(ctx.acc0.number(), ctx.acc1.number(), Value.of(10));
        
        require(value, p -> assertTrue(p >= 1));    
    }
    
    @Test
    public void should_correctly_transfer_some_amount_from_one_account_to_another() {
        final Context ctx = createContext();
     
        ctx.ts.transfer(ctx.acc0.number(), ctx.acc1.number(), Value.of(10));
        
        
        require(ctx.ar.account(ctx.acc0.number()), v -> assertEquals(new Value(90), v.balance()));
        require(ctx.ar.account(ctx.acc1.number()), v -> assertEquals(new Value(210), v.balance()));
    }

    @Test
    public void should_keep_old_balance_after_exception_when_amount_on_source_account_is_not_enough() {
        final Context ctx = createContext();

        ctx.ts.transfer(ctx.acc0.number(), ctx.acc1.number(), new Value(110));
        
        require(ctx.ar.account(ctx.acc0.number()), v -> assertEquals(new Value(100), v.balance()));
        require(ctx.ar.account(ctx.acc1.number()), v -> assertEquals(new Value(200), v.balance()));
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
