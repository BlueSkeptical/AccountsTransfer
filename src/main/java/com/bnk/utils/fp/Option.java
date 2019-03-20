package com.bnk.utils.fp;

import java.util.function.Function;

public abstract class Option<T> {
    
    public static <T> Option<T> success(T value) {
       return new Success<>(value); 
    }
    
    public static <T> Option<T> empty() {
       return new Empty<>(); 
    }
   
    public abstract <S> Option<S> map(Function<T,S> fun);
    
    public abstract <S> Option<S> flatMap(Function<T,Option<S>> fun);
    
    public abstract void tryIO(Function<T, IO<Void>> success, Function<String, IO<Void>> empty);
    
    private static class Success<T> extends Option<T> {
        private final T value;
        
        public Success(T value) {
            this.value = value;
        }

        @Override
        public <S> Option<S> map(Function<T, S> fun) {
            return success(fun.apply(value));
        }

        @Override
        public <S> Option<S> flatMap(Function<T, Option<S>> fun) {
            return fun.apply(value);
        }  

        @Override
        public void tryIO(Function<T, IO<Void>> success, Function<String, IO<Void>> failure) {
            success.apply(value);
        }
    }
    
    private static class Empty<T> extends Option<T> {

        @Override
        public <S> Option<S> map(Function<T, S> fun) {
            return new Empty<>();
        }

        @Override
        public <S> Option<S> flatMap(Function<T, Option<S>> fun) {
            return empty();
        }

        @Override
        public void tryIO(Function<T, IO<Void>> success, Function<String, IO<Void>> empty) {
            empty.apply("Empty");
        }    
    }
    
}
