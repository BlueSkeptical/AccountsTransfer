package com.bnk.accounts;

import java.util.Optional;

/**
 * A service for retrieving accounts
 */
public interface AccountsRepository
{
    /**
     * Gets an account by its ID
     * @param id ID
     * @return an instance of Account
     */
    Optional<Account> account(int id);
}
