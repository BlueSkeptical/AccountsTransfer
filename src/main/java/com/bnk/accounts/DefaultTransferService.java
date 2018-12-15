package com.bnk.accounts;


public class DefaultTransferService implements TransferService {
    private final AccountsRepository accountsRepository;
    public DefaultTransferService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }
    //TODO introduce Transfer, delegate to Transfer.execute()
    @Override 
    public void transfer(int fromId, int toId, long amount) throws TransferException {
        final Account fromAccount = accountsRepository.account(fromId).orElseThrow(() -> new TransferException());
        
        if (fromAccount.balance() < amount) {
            throw new TransferException();
        }
        
        final Account toAccount = accountsRepository.account(toId).orElseThrow(() -> new TransferException());
        

        fromAccount.deposit(-amount);
        toAccount.deposit(amount);
    }
}
