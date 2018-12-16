package com.bnk.accounts;

/**
 * An account type
 * The balance read value and balance change operations parameters are represented as long integer values
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
     * @throws NotAuhtorizedException if not allowed to read balance
     */
    long balance() throws NotAuhtorizedException;

    /**
     * The operation to add some amount to this account
     * @param value to add
     * @throws NotAuhtorizedException
     * @throws TransferException 
     */
    void deposit(long value) throws NotAuhtorizedException, TransferException;
    
    /**
     * The operation to remove some amount from this account
     * @param value to take from 
     * @throws NotAuhtorizedException
     * @throws TransferException 
     */
    void withdraw(long value) throws NotAuhtorizedException, TransferException;
    
    
    /**
     * The operation to move some value from one account to an another
     * @param to account to send value
     * @param value to move from this account to an another  
     * @throws NotAuhtorizedException
     * @throws TransferException 
     */
    void transferTo(Account to, long value) throws NotAuhtorizedException, TransferException;
}
