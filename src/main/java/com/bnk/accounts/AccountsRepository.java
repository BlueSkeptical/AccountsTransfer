package com.bnk.accounts;

import java.util.Collection;
import java.util.Optional;

public interface AccountsRepository
{
    Optional<Account> account(int id);

    Collection<Account> accounts();
}
