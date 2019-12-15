package sample.accounts;

import java.util.Arrays;

import org.junit.Test;

import lajkonik.fp.Try;

import static lajkonik.fp.test.Assertions.require;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SimpleAccountsRepositoryTest {
    
    private final static Account ACCOUNT_0 = new DefaultAccount(new AccountNumber(222), new OwnerName("Name", "Surname"));
    
    @Test
    public void should_query_correct_account_for_known_account_number() {
        final SimpleAccountsRepository ar = createTestRepository();
        require(ar.account(new AccountNumber(222)).run(), v -> { assertEquals(ACCOUNT_0.number(), v.number());
                                                                 assertEquals(new Value(500), v.balance()); });
    }

 
    @Test
    public void should_fail_for_unknown_account_number() {
        final SimpleAccountsRepository ar = createTestRepository();
        final Try<? extends Account> result = ar.account(new AccountNumber(333)).run();
        result.onResult(r -> { fail(); },
                        ex -> { assertTrue(ex instanceof TransferException); });
    }

    
    private static SimpleAccountsRepository createTestRepository() {
        return new SimpleAccountsRepository(Arrays.asList(ACCOUNT_0),
                                            Arrays.asList(new DefaultOrder(ACCOUNT_0.number(), new Value(500))));
    }
}