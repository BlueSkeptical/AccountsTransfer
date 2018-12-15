package com.bnk.accounts;


public interface Account {
    int id();

    long balance();

    void deposit(long value) throws TransferException;
}
