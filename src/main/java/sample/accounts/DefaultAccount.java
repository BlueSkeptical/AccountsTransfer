package sample.accounts;

import java.util.Objects;

public class DefaultAccount implements Account {
    
    public static long MAX_VALUE = Long.MAX_VALUE;
    
    private final AccountNumber number;
    private final OwnerName ownerName;
    private final Value balance;
    
    
    public DefaultAccount(AccountNumber id, OwnerName ownerName) {
        this(id, ownerName, Value.ZERO);  
    }
    
    public DefaultAccount(AccountNumber id, OwnerName ownerName, Value balance) {
        this.number = Objects.requireNonNull(id);
        this.ownerName = Objects.requireNonNull(ownerName);
        this.balance = Objects.requireNonNull(balance);
    }
    
    @Override
    public AccountNumber number() {
        return number;
    }

    @Override
    public OwnerName ownerName() {
        return ownerName;
    }
    
    @Override
    public Value balance() {
        return balance;
    }
    
    @Override
    public String toString() {
        return "Account # " + number + " Owner's name: " + ownerName;
    }
    

}
