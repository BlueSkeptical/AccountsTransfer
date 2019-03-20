package com.bnk.utils.fp;

public interface IO<T> {
    T run();
    
    static <S> IO<S> unit(S s) {
        return () -> s;
    } 
}
