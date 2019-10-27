
package sample.utils;

import lajkonik.fp.*;

public class ThreadSafeExecution {

    public <T> Try<T> execute(IO<T> io) {
        Try<T> result=null;
        synchronized(this) {
            result = io.run();
        }
        assert result != null;
        return result;
    }

}