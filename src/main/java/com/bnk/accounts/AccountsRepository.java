package com.bnk.accounts;

import com.bnk.utils.fp.Try;


/**
 * A service for retrieving accounts
 */
public interface AccountsRepository extends OrdersLog, Commitable<Order>
{
    /**
     * Gets an account by its ID
     * @param id ID
     * @return an instance of Account
     */
    Try<? extends Account> account(AccountNumber id);

}
