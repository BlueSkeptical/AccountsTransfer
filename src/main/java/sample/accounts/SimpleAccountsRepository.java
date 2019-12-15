package sample.accounts;

import lajkonik.fp.IO;
import sample.utils.repository.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * In-memory 'database'
 */
public class SimpleAccountsRepository implements AccountsRepository {
    
    private final Collection<Account> accounts;
    private final List<Order> ordersLog;
    private int counter = 1;
    

    public SimpleAccountsRepository(List<Account> accounts, List<Order> ordersLog) {
        this.ordersLog = new ArrayList<>(ordersLog);
        this.accounts = new ArrayList<>(accounts);
    }

    @Override
    public IO<? extends Account> account(AccountNumber number) {
        return IO.of(()->  getAccount(number).orElseThrow(() -> new TransferException("Not found")) ); 
    }

    private Optional<Account> getAccount(AccountNumber number) {
        return accounts.stream().filter( p -> p.number().equals(number))
                                .findFirst()
                                .map(p -> new DefaultAccount(p.number(), p.ownerName(), calculateBalance(queryOrders(number))));
    }

    private static Value calculateBalance(List<Order> orders) {
        return orders.stream().map( p -> p.amount()).reduce(new Value(0), (x,y) ->  x.add(y));
    }
    
    @Override
    public List<Order> queryOrders(AccountNumber accountNumber) {
        return ordersLog.stream().filter(o -> o.accountNumber().equals(accountNumber)).collect(Collectors.toList());
    }

    @Override
    public IO<Integer> prepareToCommit(Transaction<Order> transaction) {
        return IO.of(() -> { 
                transaction.transactionLog().forEach((o) -> {
                    ordersLog.add(o);
                });
                return counter++;
        }); 
    }
}
