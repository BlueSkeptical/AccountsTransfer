package com.bnk.accounts;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;


/**
 * Created by ThinkPad on 12/12/2018.
 */
public class SimpleAccountsRepository implements AccountsRepository {
    
    private final Collection<Account> accounts;
    
    public SimpleAccountsRepository(Account... accounts) {
        this.accounts = Arrays.asList(accounts);
    }

    @Override
    public Optional<Account> account(int id) {
        return accounts.stream().filter( p -> p.id() == id ).findFirst();
    }
}
