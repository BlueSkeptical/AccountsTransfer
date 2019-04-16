package com.bnk.utils.fp;

import java.util.function.Function;

public abstract class Try<T> {
    
    public static <T> Try<T> success(T value) {
       return new Success<>(value); 
    }
    
    public static <T> Try<T> failure(Exception exception) {
       return new Failure<>(exception); 
    }
       
    public abstract <S> Try<S> map(Function<T,S> fun);
    
    public abstract <S> Try<S> flatMap(Function<T,Try<S>> fun);
    
    public abstract <S> IO<S> toIO(Function<T, IO<S>> success, Function<Exception, IO<S>> failure);
    
    public abstract void onResult(Function<T, IO<Nothing>> success, Function<Exception, IO<Nothing>> failure);
    
    public abstract T getElseThrow(RuntimeException t);
    
    public abstract T getElse(T elseValue);
    
    
    private static class Success<T> extends Try<T> {
        private final T value;
        
        public Success(T value) {
            this.value = value;
        }

        @Override
        public <S> Try<S> map(Function<T, S> fun) {
            return success(fun.apply(value));
        }

        @Override
        public <S> Try<S> flatMap(Function<T, Try<S>> fun) {
            return fun.apply(value);
        }  

        @Override
        public <S> IO<S> toIO(Function<T, IO<S>> success, Function<Exception, IO<S>> failure) {
            return success.apply(value);
        }
        
        @Override
        public void onResult(Function<T, IO<Nothing>> success, Function<Exception, IO<Nothing>> failure) {
            success.apply(value).run(p -> {});
        }
        
        @Override
        public T getElseThrow(RuntimeException t) {
            return value;
        }

        @Override
        public T getElse(T elseValue) {
            return value;        }
    }
    
    private static class Failure<T> extends Try<T> {

        private final Exception exception;
        
        public Failure(Exception exception) {
            this.exception = exception;
        }

        @Override
        public <S> Try<S> map(Function<T, S> fun) {
            return failure(exception);
        }

        @Override
        public <S> Try<S> flatMap(Function<T, Try<S>> fun) {
            return failure(exception);
        }

        @Override
        public <S> IO<S> toIO(Function<T, IO<S>> success, Function<Exception, IO<S>> fail) {
            return fail.apply(exception);
        }
        
        @Override
        public void onResult(Function<T, IO<Nothing>> success, Function<Exception, IO<Nothing>> failure) {
            failure.apply(exception).run(p -> {});
        }

        @Override
        public T getElseThrow(RuntimeException t) {
            throw t;
        }

        @Override
        public T getElse(T elseValue) {
           return elseValue;
        }
    }
}
