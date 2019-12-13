package sample.accounts;

import java.util.Objects;

public class DefaultOrder implements Order {

    private final AccountNumber accountNumber;
    private final Value amount;

    public DefaultOrder(AccountNumber accountNumber, Value amount) {
       this.accountNumber = Objects.requireNonNull(accountNumber);
       this.amount = Objects.requireNonNull(amount);
    }
    
    @Override
    public AccountNumber accountNumber() {
        return accountNumber;
    }

    @Override
    public Value amount() {
        return amount;
    }
    
}
