package com.bnk.accounts;

import java.util.Arrays;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

public class TransferServiceTests {

    @Test
    public void should_correctly_transfer_some_amount_from_one_account_to_another() throws TransferException, NotAuhtorizedException {

        final Account account0 = Account.newInstance(new AccountNumber(0), new OwnerName("Joe", "Doe"));
        final Account account1 = Account.newInstance(new AccountNumber(1), new OwnerName("Mary", "Smith"));
        final Order someInitialMoneyOrder0 = new DefaultOrder(account0.number(), new Value(100));
        final Order someInitialMoneyOrder1 = new DefaultOrder(account1.number(), new Value(200));
        final AccountsRepository ar = new SimpleAccountsRepository(Arrays.asList(someInitialMoneyOrder0, someInitialMoneyOrder1), account0, account1);
        final TransferService transferService = new DefaultTransferService(ar);

        final Value transfered = transferService.transfer(account0.number(), account1.number(), new Value(10));

        assertEquals(new Value(10), transfered);

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

        try {
            final Value transfered = transferService.transfer(account0.number(), account1.number(), new Value(110));
        } catch (Exception ex) {
            assertEquals(new Value(100), ar.account(account0.number()).balance());
            assertEquals(new Value(200), ar.account(account1.number()).balance());
            return;
        }
        Assert.fail();

    }

}
