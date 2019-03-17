package com.bnk.accounts;

import java.util.List;

/**
 *
 * @author ThinkPad
 */
public interface OrdersLog {
    
    List< Order > queryForAccount(AccountNumber accountNumber);
}
