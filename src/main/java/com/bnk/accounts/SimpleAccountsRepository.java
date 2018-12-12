package com.bnk.accounts;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by ThinkPad on 12/12/2018.
 */
public class SimpleAccountsRepository implements AccountsRepository {
    
    private final List<Account> accounts;
    
    public SimpleAccountsRepository(Account... accounts) {
        this.accounts = Arrays.asList(accounts);
    }

    @Override
    public Account account(int id) {
        return accounts.get(id);
    }

    @Override
    public Collection<Account> accounts() {
        return accounts;
    }
}
