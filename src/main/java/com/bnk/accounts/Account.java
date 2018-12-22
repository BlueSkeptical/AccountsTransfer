package com.bnk.accounts;

import com.bnk.utils.Result;

/**
 * An account type
 */
public interface Account {
    
    /**
     * The account unique ID
     * @return integer ID
     */
    int id();

    /**
     * Current amount on the account
     * @return a long integer number representing number of minimal fractional monetary units
     */
    Result<Value> balance();

    /**
     * The operation to add some amount to this account
     * @param value to add
     * @return 
     */
    Result<Void> deposit(Value value);
    
    /**
     * The operation to remove some amount from this account
     * @param value to take from 
     * @return  
     */
    Result<Void> withdraw(Value value);
    
    
    /**
     * The operation to move some value from one account to an another
     * @param to account to send value
     * @param value to move from this account to an another  
     * @return   
     */
    Result<Void> transferTo(Account to, Value value);
}
