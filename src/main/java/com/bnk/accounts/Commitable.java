
package com.bnk.accounts;

import com.bnk.utils.fp.Try;
import com.bnk.utils.repository.Transaction;


interface Commitable<T> {
    
    Try<Integer> commit(Transaction<T> transaction);
    
}
