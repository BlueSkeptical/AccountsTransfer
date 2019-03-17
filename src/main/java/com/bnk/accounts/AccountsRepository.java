package com.bnk.accounts;


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
    Account account(AccountNumber id);

}
