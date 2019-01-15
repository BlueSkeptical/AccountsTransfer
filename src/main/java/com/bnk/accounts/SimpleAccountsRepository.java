package com.bnk.accounts;

import com.bnk.utils.Result;
import java.util.Arrays;
import java.util.Collection;


/**
 * An implementation of AccountsRepository with linear search for accounts
 */
public class SimpleAccountsRepository implements AccountsRepository {
    
    private final Collection<Account> accounts;
    
    /**
     * @param accounts should be provided on construction time
     */
    public SimpleAccountsRepository(Account... accounts) {
        this.accounts = Arrays.asList(accounts);
    }

    @Override
    public Result<Account> account(AccountNumber id) {
        return Result.of(accounts.stream().filter( p -> p.id().num == id.num ).findFirst());
    }
}
