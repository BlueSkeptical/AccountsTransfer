package com.bnk.utils.fp;

public interface IO<T> {
    T run();
    
    IO<Nothing> empty = () -> Nothing.instance;
    
    static IO<Nothing> effect(Runnable commands) {
        return () -> { commands.run();
                       return Nothing.instance; };
    };
    
    static <S> IO<S> unit(S s) {
        return () -> s;
    } 
}
