
package com.bnk.utils.repository;

import lajkonik.fp.IO;


public interface Commitable<T> {
    
    IO<Integer> commit(Transaction<T> transaction);
    
}
