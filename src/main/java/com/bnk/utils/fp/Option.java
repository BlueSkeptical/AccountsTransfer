package com.bnk.utils.fp;

import java.util.function.Function;

public abstract class Option<T> {
    
    public static <T> Option<T> some(T value) {
       return new Success<>(value); 
    }
    
    public static <T> Option<T> empty() {
       return new Empty<>(); 
    }
   
    public abstract <S> Option<S> map(Function<T,S> fun);
    
    public abstract <S> Option<S> flatMap(Function<T,Option<S>> fun);
    
    public abstract <V> V foldLeft(V identity, Function<V, Function<T, V>> f);
    
    public abstract <V> V foldRight(V identity, Function<T, Function<V, V>> f);
    
    public abstract IO<Nothing> tryIO(Function<T, IO<Nothing>> success, Function<String, IO<Nothing>> empty);
    
    private static class Success<T> extends Option<T> {
        private final T value;
        
        public Success(T value) {
            this.value = value;
        }

        @Override
        public <S> Option<S> map(Function<T, S> fun) {
            return some(fun.apply(value));
        }

        @Override
        public <S> Option<S> flatMap(Function<T, Option<S>> fun) {
            return fun.apply(value);
        }  

        @Override
        public IO<Nothing> tryIO(Function<T, IO<Nothing>> success, Function<String, IO<Nothing>> failure) {
            return success.apply(value);
        }
        
         @Override
        public <V> V foldLeft(V identity, Function<V, Function<T, V>> f) {
            return f.apply(identity).apply(value);
        }

        @Override
        public <V> V foldRight(V identity, Function<T, Function<V, V>> f) {
            return f.apply(value).apply(identity);
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
        public IO<Nothing> tryIO(Function<T, IO<Nothing>> success, Function<String, IO<Nothing>> empty) {
            return empty.apply("Empty");
        }    

        @Override
        public <V> V foldLeft(V identity, Function<V, Function<T, V>> f) {
            return identity;
        }

        @Override
        public <V> V foldRight(V identity, Function<T, Function<V, V>> f) {
            return identity;
        }
    }
    
}
