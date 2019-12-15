
package sample.utils.repository;

import lajkonik.fp.IO;


public interface Commitable<T> {
    
    IO<Integer> prepareToCommit(Transaction<T> transaction);
    
}
