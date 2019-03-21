package com.bnk.accounts;

import com.bnk.utils.fp.IO;
import com.bnk.utils.fp.Nothing;
import com.bnk.utils.fp.Try;
import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.Test;

public class TransferServiceTests {

    @Test
    public void should_correctly_transfer_some_amount_from_one_account_to_another() {

        final Account account0 = Account.newInstance(new AccountNumber(0), new OwnerName("Joe", "Doe"));
        final Account account1 = Account.newInstance(new AccountNumber(1), new OwnerName("Mary", "Smith"));
        final Order someInitialMoneyOrder0 = new DefaultOrder(account0.number(), new Value(100));
        final Order someInitialMoneyOrder1 = new DefaultOrder(account1.number(), new Value(200));
        final AccountsRepository ar = new SimpleAccountsRepository(Arrays.asList(someInitialMoneyOrder0, someInitialMoneyOrder1), account0, account1);
        final TransferService ts = new DefaultTransferService(ar);

        final Try<Value> value = ts.transfer(account0.number(), account1.number(), Value.of(10));
        
        value.io(v -> IO.effect(() ->  assertEquals(Value.of(10), v)),
                 ex -> IO.effect(() -> fail(ex.getMessage()))).run();
        
        assertEquals(new Value(90), ar.account(account0.number()).balance());
        assertEquals(new Value(210), ar.account(account1.number()).balance());
    }

    @Test
    public void should_keep_old_balance_after_exception_when_amount_on_source_account_is_not_enough() {
        final Account account0 = Account.newInstance(new AccountNumber(0), new OwnerName("Joe", "Doe"));
        final Account account1 = Account.newInstance(new AccountNumber(1), new OwnerName("Mary", "Smith"));
        final Order someInitialMoneyOrder0 = new DefaultOrder(account0.number(), new Value(100));
        final Order someInitialMoneyOrder1 = new DefaultOrder(account1.number(), new Value(200));
        final AccountsRepository ar = new SimpleAccountsRepository(Arrays.asList(someInitialMoneyOrder0, someInitialMoneyOrder1), account0, account1);
        final TransferService transferService = new DefaultTransferService(ar);

        final Try<Value> transfered = transferService.transfer(account0.number(), account1.number(), new Value(110));
        transfered.io(v -> { fail("Unexpected value"); 
                            return null;},
                         ex -> { assertEquals(new Value(100), ar.account(account0.number()).balance());
                                 assertEquals(new Value(200), ar.account(account1.number()).balance());
                                 return null;});

    }

}
