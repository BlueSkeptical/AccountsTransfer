package com.bnk.accounts;

import com.bnk.utils.fp.Try;
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
    public synchronized Try<Value> transfer(AccountNumber accountFrom, AccountNumber to, Value amount) {
        
        try {
            final Transaction<Order> withdrawTransaction = withdrawOrder(repository.account(accountFrom), amount);
            final Transaction<Order> resultTransaction = withDepositOrder(withdrawTransaction, repository.account(to), amount);    
            repository.commit(resultTransaction);
            return Try.success(amount);
        } catch(Exception ex) {
            return Try.failure(ex);
        }
        

    }

    @Override
    public synchronized Account withdraw(Account from, Value amount) {
        try {
            final Transaction<Order> transaction = withdrawOrder(from, amount);
            repository.commit(transaction);
        } catch (Exception ex) {
            ////
        }
        return repository.account(from.number());
    }

    @Override
    public synchronized Account deposit(Account to, Value amount) {
        try {
            final Transaction<Order> transaction = withDepositOrder(Transaction.empty(), to, amount);
            repository.commit(transaction);
        } catch (Exception ex) {
            ////
        }
        return repository.account(to.number());
    }

    private Transaction<Order> withDepositOrder(Transaction<Order> transaction, Account to, Value amount) {
            final Order depositOrder = new DefaultOrder(to.number(), amount);
            return transaction.add(depositOrder);
    }
    
    private Transaction<Order> withdrawOrder(Account from, Value amount) {
        if (from.balance().compareTo(amount) >= 0) {
            final Order order = new DefaultOrder(from.number(), amount.negate());
            return Transaction.empty().add(order);
        } else {
            throw new IllegalStateException();
        }
    }

}
