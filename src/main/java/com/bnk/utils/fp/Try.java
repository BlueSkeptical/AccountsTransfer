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
    
    public abstract void tryIO(Function<T, IO<Void>> success, Function<Exception, IO<Void>> failure);
    
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
        public void tryIO(Function<T, IO<Void>> success, Function<Exception, IO<Void>> failure) {
            success.apply(value);
        }
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
        public void tryIO(Function<T, IO<Void>> success, Function<Exception, IO<Void>> failure) {
            failure(exception);
        }
    }
}
