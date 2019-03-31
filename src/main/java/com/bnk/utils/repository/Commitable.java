
package com.bnk.utils.repository;

import com.bnk.utils.fp.IO;


public interface Commitable<T> {
    
    IO<Integer> commit(Transaction<T> transaction);
    
}
