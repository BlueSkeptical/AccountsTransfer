package com.bnk.accounts;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;


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
    public Optional<Account> account(int id) {
        return accounts.stream().filter( p -> p.id() == id ).findFirst();
    }
}
