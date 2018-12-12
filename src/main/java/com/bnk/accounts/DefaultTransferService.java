package com.bnk.accounts;

/**
 * Created by ThinkPad on 12/12/2018.
 */
public class DefaultTransferService implements TransferService {
    private final AccountsRepository accountsRepository;
    public DefaultTransferService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    @Override
    public void transfer(int fromId, int toId, long amount) throws TransferException {
        final Account fromAccount = accountsRepository.account(fromId);
        
        if (fromAccount.balance() < amount) {
            throw new TransferException();
        }
        
        final Account toAccount = accountsRepository.account(toId);
        

        fromAccount.deposit(-amount);
        toAccount.deposit(amount);
    }
}
