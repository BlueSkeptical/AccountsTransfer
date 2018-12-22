package com.bnk.utils;

public interface Void {
    static Void INSTANCE = new JustVoid();
    
    class JustVoid implements Void {
    }
}
