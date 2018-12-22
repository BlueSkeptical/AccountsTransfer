package com.bnk.utils;

import java.util.Optional;
import java.util.function.Function;

public interface Result<T> {
    
    void verify(Effect<T> onSuccess, Effect<Exception> onFail);

    
    <S> Result<S> mapValue(Function<T, Result<S>> fun);
    
    <S, T2> Result<S> with(Result<T2> that,
                          Function<T, Function<T2, Result<S>>> fun);
    
    
    static <S> Result<S> of(Optional<S> optional) {
        return optional.isPresent() ? new Result.Success<>(optional.get()) : new Result.Fail<>(new NullPointerException());
    }
    
    interface Void { 
        static Void INSTANCE = new JustVoid();
        
        class JustVoid implements Void {
        }
    }
    
 
    
    
    class Success<T> implements Result<T> {
        public final T resultValue;
        
        public Success() {
            this.resultValue = (T) Void.INSTANCE;
        }
        
        public Success(T resultValue){
            this.resultValue = resultValue;
        }

        @Override
        public void verify(Effect<T> onSuccess, Effect<Exception> onFail) {
            onSuccess.exec(resultValue);
        }
        
        @Override
        public boolean equals(Object that) {
            if (that instanceof Success) {
                return ((Success)that).resultValue.equals(this.resultValue);
            }
            return false;
        }

        @Override
        public <S, T2> Result<S> with(Result<T2> that, Function<T, Function<T2, Result<S>>> fun) {
            return that.mapValue(fun.apply(this.resultValue));
        }

        @Override
        public <S> Result<S> mapValue(Function<T, Result<S>> fun) {
            return fun.apply(this.resultValue);
        }
    }
    
    class Fail<Void> implements Result<Void> {
        
        public final Exception ex;
        
        public Fail(Exception ex) {
          this.ex = ex;  
        }

        @Override
        public void verify(Effect<Void> onSuccess, Effect<Exception> onFail) {
            onFail.exec(ex);
        }
        
        @Override
        public boolean equals(Object that) {
            if (that instanceof Fail) {
                final Exception thatException = ((Fail)that).ex;
                return ex.getClass().equals(thatException.getClass());
            }
            return false;
        }

        @Override
        public <S, T2> Result<S> with(Result<T2> that, Function<Void, Function<T2, Result<S>>> fun) {
            return new Result.Fail<>(ex);
        }

        @Override
        public <S> Result<S> mapValue(Function<Void, Result<S>> fun) {
            return new Result.Fail<>(ex);
        }
    }
}
