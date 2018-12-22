package com.bnk.accounts;

import com.bnk.utils.Result;
import static org.junit.Assert.*;
import org.junit.Test;

public class TransferServiceTests {

    @Test
    public void should_correctly_transfer_some_amount_from_one_account_to_another() throws TransferException, NotAuhtorizedException {
        final TransferService transferService = new DefaultTransferService();
        
        final Account account0 = new DefaultAccount(0, new Value(100), transferService);
        final Account account1 = new DefaultAccount(1, new Value(200), transferService);
        
        final Result<Void> result = account0.transferTo(account1, new Value(10));
        
        assertEquals(new Result.Success<>(), result);
        
        
        assertEquals(new Result.Success<>(new Value(90)), account0.balance());
        assertEquals(new Result.Success<>(new Value(210)), account1.balance());
    }
    
    
    @Test
    public void should_keep_old_balance_after_exception_when_amount_on_source_account_is_not_enough() {
        final TransferService transferService = new DefaultTransferService();
        
        final Account account0 = new DefaultAccount(0, new Value(100), transferService);
        final Account account1 = new DefaultAccount(1, new Value(200), transferService);
        
        

        final Result<Void> result = account0.transferTo(account1, new Value(101));
        
        assertEquals(new Result.Fail<>(new TransferException()), result);

        assertEquals(new Result.Success<>(new Value(100)), account0.balance());
        assertEquals(new Result.Success<>(new Value(200)), account1.balance());
    }
    
    @Test
    public void should_throw_exception_when_amount_on_destination_account_after_transfer_will_cause_overflow() {
        final TransferService transferService = new DefaultTransferService();
        
        final Account account0 = new DefaultAccount(0, new Value(101), transferService);
        final Account account1 = new DefaultAccount(1, new Value(DefaultAccount.MAX_VALUE - 99), transferService);
        
        final Result<Void> result = account0.transferTo(account1, new Value(101));
        assertEquals(new Result.Fail<>(new TransferException()), result);
    }
   
}
