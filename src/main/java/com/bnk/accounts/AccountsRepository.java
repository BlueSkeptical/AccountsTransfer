package com.bnk.accounts;

import com.bnk.utils.Result;

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
    Result<Account> account(int id);
}
