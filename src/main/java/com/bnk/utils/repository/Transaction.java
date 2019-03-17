
package com.bnk.utils.repository;

import java.util.Collections;
import java.util.List;


public interface Transaction<T> {
    
    Transaction<T> log( T command);
    
    List<T> transactionLog();
    
    static Transaction newInstace() {
        return new SimpleTransaction<>( Collections.EMPTY_LIST );
    }
}
