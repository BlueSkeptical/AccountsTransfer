package com.bnk.accounts;

/**
 *
 * @author ThinkPad
 */
public interface Order {
    
    AccountNumber accountNumber();
    
    Value amount();
}
