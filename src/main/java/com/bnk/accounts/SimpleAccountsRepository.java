package com.bnk.accounts;

import com.bnk.utils.fp.Try;
import com.bnk.utils.repository.Transaction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * In-memory 'database'
 */
public class SimpleAccountsRepository implements AccountsRepository {
    
    private final Collection<Account> accounts;
    private final List<Order> ordersLog;
    private int counter = 1;
    

    public SimpleAccountsRepository(List<Order> ordersLog, Account... accounts) {
        this.ordersLog = new ArrayList<>(ordersLog);
        this.accounts = Arrays.asList(accounts);
    }

    @Override
    public Try<? extends Account> account(AccountNumber number) {
        return accounts.stream().filter( p -> p.number().equals(number))
                                .findFirst()
                                .map(p -> Try.success(new DefaultAccount(p.number(), p.ownerName(), queryForAccount(number))))
                                .orElse(Try.failure(new IllegalStateException()));
    }


    @Override
    public List<Order> queryForAccount(AccountNumber accountNumber) {
        return ordersLog.stream().filter(o -> o.accountNumber().equals(accountNumber)).collect(Collectors.toList());
    }

    @Override
    public Try<Integer> commit(Transaction<Order> transaction) {
        transaction.transactionLog().forEach((o) -> {
            ordersLog.add(o);
        });
        return Try.success(counter++);
    }


}
