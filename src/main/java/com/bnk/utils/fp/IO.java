package com.bnk.utils.fp;

import java.util.function.Function;

public interface IO<T> {
    T run();
    
    IO<Nothing> empty = () -> Nothing.instance;
    
    static IO<Nothing> effect(Runnable commands) {
        return () -> { commands.run();
                       return Nothing.instance; };
    };
    
    default <B> IO<B> map(Function<T, B> f) {
        return () -> f.apply(this.run());
    }

    default <B> IO<B> flatMap(Function<T, IO<B>> f) {
        return () -> f.apply(this.run()).run();
}
    
    static <S> IO<S> unit(S s) {
        return () -> s;
    } 
}
