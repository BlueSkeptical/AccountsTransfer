package com.bnk.accounts;

/**
 * Represents a bank account
 */
public interface Account {
    int id();

    long balance();

    void deposit(long value);
}
