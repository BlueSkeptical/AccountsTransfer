package sample.accounts;

public class Value implements Comparable<Value> {
    public static final Value ZERO = new Value(0);
    public static final Value MAX = new Value(Long.MAX_VALUE);
    
    private final long value;
    
    public Value(long value){
        this.value = value;
    }
   
    public static Value of(long value) {
        return new Value(value);
    }
    
    public Value add(Value that) {
        return new Value(that.value + value);
    }
    
    public Value substract(Value that) {
        return new Value(value - that.value);
    }
    
    public Value negate() {
        return new Value(-value);
    }
    
    @Override
    public String toString() {
        return Long.toString(value);
    }
    
    @Override
    public boolean equals(Object that) {
        if ( that instanceof Value) {
            final Value thatValue = (Value)that;
            return value == thatValue.value;
        }
        return false;
        
    }

    @Override
    public int compareTo(Value o) {
        return Long.compare(value, o.value);
    }

}
