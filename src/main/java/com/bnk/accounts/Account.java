package com.bnk.accounts;

/**
 * An account type
 */
public interface Account {
    
    /**
     * The account unique ID
     * @return integer ID
     */
    AccountNumber id();

    
    /**
     * Owner's name
     * 
     * @return owner name
     */
    OwnerName ownerName();
    
    /**
     * Current amount on the account
     * @return a long integer number representing number of minimal fractional monetary units
     */
    Value balance();
    
    
    void setBalance(Value amount);

}
