package com.bnk.accounts;

import com.bnk.utils.fp.IO;
import com.bnk.utils.fp.Try;
import com.bnk.utils.repository.Transaction;

public class DefaultTransferService implements TransferService {

    private final AccountsRepository repository;

    public DefaultTransferService(AccountsRepository repository) {
        this.repository = repository;
    }

    @Override
    public synchronized IO<Try<Integer>> transfer(AccountNumber from, AccountNumber to, Value amount) {
        
        final IO<Try<? extends Account>> accountFrom = () -> repository.account(from);
        
        return accountFrom.flatMap(p -> () -> withdrawOrder(p, amount))
                          .flatMap(p -> () -> depositOrder(p, repository.account(to), amount))
                          .flatMap(p -> () -> p.flatMap(s -> repository.commit(s)));
    }

    private Try<Transaction<Order>> depositOrder(Try<Transaction<Order>> transaction, Try<? extends Account> to, Value amount) {
        return to.flatMap(p -> transaction.flatMap(t -> Try.success(t.add(new DefaultOrder(p.number(), amount)))));
    }
    
    private Try<Transaction<Order>> withdrawOrder(Try<? extends Account> from, Value amount) {
        return from.flatMap(p -> p.balance().compareTo(amount) >= 0 ?
                Try.success(Transaction.empty().add(new DefaultOrder(p.number(), amount.negate()))) : Try.failure(new IllegalStateException()));
    }

}
