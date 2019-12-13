package sample.accounts;

import java.util.List;

public interface OrdersLog {
    
    List<Order> queryForAccount(AccountNumber accountNumber);
}
