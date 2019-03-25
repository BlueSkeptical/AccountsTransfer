
package com.bnk.utils.repository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleTransaction<T> implements Transaction<T> {
    
    private final List<T> log;
    
    SimpleTransaction( List<T> log )
    {
        this.log = log;
    }
    
    public static Transaction newInstace() {
        return new SimpleTransaction<>( Collections.EMPTY_LIST );
    }
    
    @Override
    public List<T> transactionLog() {
        return log;
    }

    @Override
    public Transaction<T> add(T command) {
        final List<T> l = new ArrayList<>(log);
        l.add(command);
        return new SimpleTransaction<>(l);
    }
    
}
