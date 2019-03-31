package com.bnk.utils.fp;

import java.util.function.Function;

public class IO<T> {
    private final Task<T> task;
    
    private IO(Task<T> task) {
        this.task = task;
    }
    
    public static <S> IO<S> of(Task<S> f) {
        return new IO(f);
    }
    
    public static IO<Nothing> empty = IO.of(() -> Nothing.instance);
    
    public static IO<Nothing> effect(Runnable commands) {
        return IO.of(() -> { commands.run();
                             return Nothing.instance; });
    };
    
    public <B> IO<B> map(Function<T, B> f) {
        return IO.of(() -> {
            
            return f.apply(task.run());
        });
    }

    public <B> IO<B> flatMap(Function<T, IO<B>> f) {
        return IO.of(() -> {
            return f.apply(task.run()).task.run();
        });
    }
    
    public static <S> IO<S> unit(S s) {
        return IO.of(() -> s);
    } 
    
    public void onCallback(Callback<T> f) {
        f.callback(Try.of(task));
    }
    
    public interface Task<S> {
        S run();
    }
    
    public interface Callback<S> {
        void callback(Try<S> f);
    }
    
}
