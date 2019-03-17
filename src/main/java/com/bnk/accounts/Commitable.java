
package com.bnk.accounts;

import com.bnk.utils.repository.Transaction;


interface Commitable<T> {
    
    void commit(Transaction<T> transaction);
    
}
