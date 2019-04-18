package com.bnk.utils.fp;

import static com.bnk.utils.fp.Try.failure;
import static com.bnk.utils.fp.Try.success;
import java.util.function.Function;

public class IO<T> {
    
    private static final Object FAILED_RESULT = new Object();
    
    public static final Exception FAILED_RESULT_EXCEPION = new FailedException();
    
    public static final IO<Nothing> EMPTY = IO.of(() -> Nothing.instance);
    
    private final Task<T> task;
    
    private IO(Task<T> task) {
        this.task = task;
    }
    
    public static <S,E extends Throwable> IO<S> of(CheckedTask<S, E> f) {
        return new IO(uncheck(f));        
    }
    
    public static IO<Nothing> effect(CheckedRunnable f) {
        return IO.of(() -> { f.run();
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
    
    public static <S> S fail() {
        return (S) FAILED_RESULT;
    }
    
    public void run(Callback<T> f) {  
        Try<T> result;
        try {
            result = success(task.run());
        } catch (Exception ex) {
            result = failure(ex);
        }
        if (result == FAILED_RESULT) {
            f.callback(failure(FAILED_RESULT_EXCEPION));    
        }
        else {
            f.callback(result);
        }
    }
    
    public interface Task<S> {
        S run();
    }
    
    public interface CheckedTask<S,E extends Throwable> {
        S run() throws E;
    }
    
    public interface CheckedRunnable<E extends Throwable> {
        void run() throws E;
    }
    
    public interface Callback<S> {
        void callback(Try<S> f);
    }
    
    public static class FailedException extends RuntimeException {     
    }
    
    private static <S, E extends Throwable> Task uncheck(CheckedTask<S, E> f) {
        return () -> {
            try {
                return f.run();
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
