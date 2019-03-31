package com.bnk.utils.fp;

import java.util.function.Consumer;

public class Assertions {
    
    public static <T> void require(IO<T> result, Consumer<T> assertion) {
        result.onCallback(r -> assertion.accept(r.getElseThrow(new IllegalStateException())));
    }
}
