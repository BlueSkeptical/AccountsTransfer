package sample.accounts;

/**
 * An account type
 */
public interface Account {
    
    /**
     * The account unique ID
     * @return integer ID
     */
    AccountNumber number();

    
    /**
     * The owner's name
     * 
     * @return owner name
     */
    OwnerName ownerName();
    
    /**
     * Current amount on the account
     * @return a long integer number representing number of minimal fractional monetary units
     */
    Value balance();
    
    
    public static Account newInstance(AccountNumber id, OwnerName ownerName) {
        return new DefaultAccount(id, ownerName);
    }
    
    public static Account snapshot(AccountNumber id, OwnerName ownerName, Value p_balance) {
        return new DefaultAccount(id, ownerName, p_balance);
    }

}
