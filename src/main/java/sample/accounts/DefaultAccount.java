package sample.accounts;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DefaultAccount implements Account {
    
    public static long MAX_VALUE = Long.MAX_VALUE;
    
    private final AccountNumber number;
    private final OwnerName ownerName;
    private final List<Order> balanceTransactions;
    
    
    public DefaultAccount(AccountNumber id, OwnerName ownerName) {
        this(id, ownerName, Collections.EMPTY_LIST );  
    }
    
    public DefaultAccount(AccountNumber id, OwnerName ownerName, List<Order> balanceTransactions) {
        this.number = Objects.requireNonNull(id);
        this.ownerName = Objects.requireNonNull(ownerName);
        this.balanceTransactions = Objects.requireNonNull(balanceTransactions);
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
        return balanceTransactions.stream().map( p -> p.amount()).reduce(new Value(0), (x,y) ->  x.add(y));
    }
    
    @Override
    public String toString() {
        return "Account # " + number + " Owner's name: " + ownerName;
    }
    

}
