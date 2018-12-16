package com.bnk.accounts;

/**
 * An account type
 * The balance read value and balance change operations parameters are represented as long integer values
 */
public interface Account {
    
    /**
     * The account's unique ID
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
     * The operation to put some amount on the account
     * @param value
     * @throws NotAuhtorizedException
     * @throws TransferException 
     */
    void deposit(long value) throws NotAuhtorizedException, TransferException;
    
    /**
     * The operation to remove some amount from the account
     * @param value
     * @throws NotAuhtorizedException
     * @throws TransferException 
     */
    void withdraw(long value) throws NotAuhtorizedException, TransferException;
    
    
    /**
     * The operation to move some value from one account to an another
     * @param to
     * @param value
     * @throws NotAuhtorizedException
     * @throws TransferException 
     */
    void transferTo(Account to, long value) throws NotAuhtorizedException, TransferException;
}
