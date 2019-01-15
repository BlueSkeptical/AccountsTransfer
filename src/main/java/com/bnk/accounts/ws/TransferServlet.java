
package com.bnk.accounts.ws;

import com.bnk.accounts.Account;
import com.bnk.accounts.AccountNumber;
import com.bnk.accounts.AccountsRepository;
import com.bnk.accounts.TransferService;
import com.bnk.accounts.Value;
import com.bnk.utils.Result;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TransferServlet extends HttpServlet {
    private final AccountsRepository accountsRepository;
    private final TransferService transferDelegate;
    
    public TransferServlet(AccountsRepository accountsRepository, TransferService transferDelegate) {
        this.accountsRepository = accountsRepository;
        this.transferDelegate = transferDelegate;
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(HttpClientTransferService.CONTENT_TYPE);
        final int fromAccountId = Integer.parseInt(request.getParameter(HttpClientTransferService.FROM_ACCOUNT_PARAMETER_NAME));
        final int toAccountId = Integer.parseInt(request.getParameter(HttpClientTransferService.TO_ACCOUNT_PARAMETER_NAME));
        final Value amount = new Value(Long.parseLong(request.getParameter(HttpClientTransferService.AMOUNT_PARAMETER_NAME)));
        final Result<Account> fromAccount = accountsRepository.account(new AccountNumber(fromAccountId));
        final Result<Account> toAccount = accountsRepository.account(new AccountNumber(toAccountId));
        synchronized(accountsRepository) {  
            fromAccount.with(toAccount, from -> to -> { transfer(from, to, amount, response); 
                                                       return new Result.Success(); });
            
        }
    } 
    
    private void transfer(Account from, Account to, Value amount, HttpServletResponse response) {
        transferDelegate.transfer(from, to, amount)
                        .verify((v) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            println(response, "OK");
                        }, (ex) -> {
                            Logger.getLogger(TransferServlet.class.getName()).log(Level.WARNING, ex.getMessage());
                            println(response, "ERR:" + ex.getMessage());
                            response.setStatus(HttpServletResponse.SC_CONFLICT);
                        });
    }
    
    private void println(HttpServletResponse response, String str) {
        try { 
            response.getWriter().println(str);
        } catch (IOException ex) {
            Logger.getLogger(TransferServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
