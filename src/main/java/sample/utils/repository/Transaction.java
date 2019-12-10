
package sample.utils.repository;

import java.util.ArrayList;
import java.util.List;

import sample.accounts.Order;


public interface Transaction<T> {
    
    Transaction<T> add(T command);
    
    List<T> transactionLog();
    
    static <U extends Order> Transaction<U>  empty() {
        return new SimpleTransaction<U>(new ArrayList<U>());
    }
}
