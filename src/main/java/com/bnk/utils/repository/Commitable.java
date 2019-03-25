
package com.bnk.utils.repository;

import com.bnk.utils.fp.Try;


public interface Commitable<T> {
    
    Try<Integer> commit(Transaction<T> transaction);
    
}
