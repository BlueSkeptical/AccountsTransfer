package com.bnk.accounts;

import com.bnk.utils.fp.IO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class TestBalance {
    public static void verifyBalance(AccountsRepository ar, Account acc, Value val) {
        ar.account(acc.number()).io(a -> IO.effect(()-> assertEquals(val, a.balance())),ex -> IO.effect(() -> fail(ex.getMessage()))).run();
    }
}
