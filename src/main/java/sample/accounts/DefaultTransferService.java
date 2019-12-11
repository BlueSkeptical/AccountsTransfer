package sample.accounts;

import lajkonik.fp.IO;
import sample.utils.repository.Transaction;

public class DefaultTransferService implements TransferService {

    private final AccountsRepository repository;

    public DefaultTransferService(AccountsRepository repository) {
        this.repository = repository;
    }

    @Override
    public IO<Integer> transfer(AccountNumber from, AccountNumber to, Value amount) {
        final IO<? extends Account> accountFrom = repository.account(from);
        final IO<? extends Account> accountTo = repository.account(to);
        return accountFrom.flatMap(a -> 
                                        accountTo.flatMap(b -> 
                                                             transfer(a, b, amount)));
    }

    private IO<Integer> transfer(Account accountFrom, Account accountTo, Value amount) {
        if (canWithdraw(accountFrom, amount)) {
           return repository.commit(Transaction.empty()
                                     .add(new DefaultOrder(accountFrom.number(), amount.negate()))
                                     .add(new DefaultOrder(accountTo.number(), amount)));
        } else {
            return IO.of(() -> {throw new TransferException("Not enough money on the account"); });
        }
    }
    
    private boolean canWithdraw(Account accountFrom, Value amount) {
        return accountFrom.balance().compareTo(amount) >= 0;
    }

}
