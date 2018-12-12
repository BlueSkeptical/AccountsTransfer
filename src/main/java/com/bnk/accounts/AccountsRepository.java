package com.bnk.accounts;

import java.util.Collection;

/**
 * Created by ThinkPad on 12/12/2018.
 */
public interface AccountsRepository
{
    Account account(int id);

    Collection<Account> accounts();
}
