
package sample.utils.repository;

import java.util.Collections;
import java.util.List;


public interface Transaction<T> {
    
    Transaction<T> add( T command);
    
    List<T> transactionLog();
    
    static Transaction empty() {
        return new SimpleTransaction<>( Collections.EMPTY_LIST );
    }
}
