/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bnk.accounts;

/**
 *
 * @author ThinkPad
 */
public class TransferException extends Exception {
    
    public TransferException() {
        super();
    }
    
    public TransferException(String reason) {
        super(reason);
    }
}
