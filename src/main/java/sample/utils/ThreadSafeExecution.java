
package sample.utils;

import lajkonik.fp.*;

public class ThreadSafeExecution {

    public synchronized <T> Try<T>  execute(IO<T> io) {
        return io.run();
    }

}