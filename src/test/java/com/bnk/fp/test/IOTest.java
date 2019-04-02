package com.bnk.fp.test;

import com.bnk.utils.fp.IO;
import com.bnk.utils.fp.Nothing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class IOTest {
    
    @Test
    public void should_write_value() {
        final ReadWrite<Integer> rw = new SimpleRW(1);
        final IO<Nothing> write = writeIO(rw, 2);
        
        write.onCallback(f -> {});
        Assert.assertEquals(2, (int)rw.read());
    }
    
    @Test
    public void should_invoke_callback_on_write_with_correct_parameter() {
        final ReadWrite<Integer> rw = new SimpleRW(1);
        final ReadWrite<Integer> state = new SimpleRW(1);
        final IO<Nothing> write = writeIO(rw, 2);
        
        write.onCallback(f -> { Assert.assertEquals(f.getElseThrow(new IllegalStateException()), Nothing.instance);
                         state.write(0); });
        
        Assert.assertEquals(0, (int)state.read());
    }
    
    @Test
    public void should_read_value() {
        final ReadWrite<Integer> rw = new SimpleRW(1);
        final IO<Integer> read = readIO(rw);
        final ReadWrite<Integer> state = new SimpleRW(1);
        
        read.onCallback(f -> {Assert.assertEquals(1, (int)f.getElseThrow(new IllegalStateException()));
                              state.write(0);});
        Assert.assertEquals(0, (int)state.read());
    }
    
    @Test
    public void should_return_fail_when_exception() {
        final IO<Integer> io = IO.of(() -> {throw new RuntimeException();});
        final ReadWrite<Integer> state = new SimpleRW(1);
        
        io.onCallback(f -> {Assert.assertEquals(2, (int)f.getElse(2));
                              state.write(0);});
        Assert.assertEquals(0, (int)state.read());
    }
    
    @Test
    public void should_correctly_map() {
        final ReadWrite<String> rw = new SimpleRW("1");
        final IO<String> io = readIO(rw);
        final IO<Integer> mapped = io.map(f -> Integer.parseInt(f))
                                     .map(f -> f - 1);
        mapped.onCallback(f -> Assert.assertEquals(0, (int)f.getElseThrow(new RuntimeException())));
    }
    
    
    @Test
    public void should_correctly_flatmap() {
        final ReadWrite<String> rw = new SimpleRW("1");
        final IO<String> readIO = readIO(rw);
        final IO<Integer> flatmappedIO = readIO.flatMap(f -> IO.of(() -> Integer.parseInt(f)));
        
        final SimpleRW<Integer> state = new SimpleRW(0);
        
        final IO<Integer> readWriteIO = flatmappedIO.flatMap(f -> IO.of(() -> {state.write(f);
                                                                         return f + 1;}));
        
        final IO<Nothing> writeIO = readWriteIO.flatMap(f -> IO.effect(() -> state.write(f + 1)));
        
        Assert.assertEquals(1, state.getValues().size());
        
        writeIO.onCallback(f -> {});
        
        Assert.assertEquals(3, state.getValues().size());
        
        Assert.assertEquals(0, (int)state.getValues().get(0));
        Assert.assertEquals(1, (int)state.getValues().get(1));
        Assert.assertEquals(3, (int)state.getValues().get(2));
        
        
    }
    
    public static <S> IO<S> readIO(ReadWrite<S> rw) {
        return IO.of(() -> { return rw.read(); });
    }
    
    public static IO<Nothing> writeIO(ReadWrite rw, int v) {
        return IO.effect(() ->  rw.write(v));
    }
    
    public interface ReadWrite<S> {
        
        S read();
        
        void write(S v);
        
        
    }
    
    static class SimpleRW<S> implements ReadWrite<S> {

        private List<S> values = new ArrayList<>();
        
        public List<S> getValues() {
            return Collections.unmodifiableList(values);
        }
        
        public SimpleRW(S initialValue) {
            values.add(initialValue);
        }
        
        @Override
        public S read() {
            return values.get(values.size() - 1);
        }

        @Override
        public void write(S v) {
           values.add(v); 
        }
    
    }
}
