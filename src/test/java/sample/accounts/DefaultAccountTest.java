package sample.accounts;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class DefaultAccountTest {
   
    @Test
    public void should_have_zero_balance_with_empty_transactions_log() {
        final Account account = new DefaultAccount(new AccountNumber(1), new OwnerName("Name", "Surname"));
        assertEquals(Value.ZERO, account.balance());
    }

}