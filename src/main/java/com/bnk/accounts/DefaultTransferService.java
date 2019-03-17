package com.bnk.accounts;

import com.bnk.utils.repository.Transaction;

/**
 * An
 */
public class DefaultTransferService implements TransferService {

    private final AccountsRepository repository;

    public DefaultTransferService(AccountsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Value transfer(AccountNumber accountFrom, AccountNumber to, Value amount) {
        
        
        final Transaction<Order> transaction = logWithdrawOrder(Transaction.newInstace(), repository.account(accountFrom), amount);
        final Transaction<Order> resultTransaction = logDepositOrder(transaction, repository.account(to), amount);
        repository.commit(resultTransaction);

        return amount;

    }

    @Override
    public Account withdraw(Account from, Value amount) {
        try {
            final Transaction<Order> transaction = logWithdrawOrder(Transaction.newInstace(), from, amount);
            repository.commit(transaction);
        } catch (Exception ex) {
            ////
        }
        return repository.account(from.number());
    }

    @Override
    public Account deposit(Account to, Value amount) {
        try {
            final Transaction<Order> transaction = logDepositOrder(Transaction.newInstace(), to, amount);
            repository.commit(transaction);
        } catch (Exception ex) {
            ////
        }
        return repository.account(to.number());
    }

    private Transaction<Order> logDepositOrder(Transaction<Order> transaction, Account to, Value amount) {
            final Order depositOrder = new DefaultOrder(to.number(), amount);
            return transaction.log(depositOrder);
    }
    
    private Transaction<Order> logWithdrawOrder(Transaction<Order> transaction, Account from, Value amount) {
        if (from.balance().compareTo(amount) >= 0) {
            final Order order = new DefaultOrder(from.number(), amount.negate());
            return transaction.log(order);
        } else {
            throw new IllegalStateException();
        }
    }

}
