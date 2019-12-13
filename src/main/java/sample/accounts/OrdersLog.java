package sample.accounts;

import java.util.List;

public interface OrdersLog {
    
    List<Order> queryOrders(AccountNumber accountNumber);
}
