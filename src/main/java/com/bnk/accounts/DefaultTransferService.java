package com.bnk.accounts;

import java.util.Objects;


public class DefaultTransferService implements TransferService {
    private final AccountsRepository accountsRepository;
    
    public DefaultTransferService(AccountsRepository accountsRepository) {
        Objects.requireNonNull(accountsRepository);
        this.accountsRepository = accountsRepository;
    }

    @Override 
    public void transfer(int fromId, int toId, long amount) throws TransferException {
        synchronized(accountsRepository) {
            final Account fromAccount = accountsRepository.account(fromId).orElseThrow(() -> new TransferException("Source account not found, id=" + fromId));
            final Account toAccount = accountsRepository.account(toId).orElseThrow(() -> new TransferException("Destination account not found id=" + toId));
        
            new Transfer(fromAccount, toAccount, amount).execute(); 
        }      
    }
}
