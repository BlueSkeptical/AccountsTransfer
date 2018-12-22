
package com.bnk.accounts.ws;

import com.bnk.accounts.Account;
import com.bnk.accounts.AccountsRepository;
import com.bnk.accounts.NotAuhtorizedException;
import com.bnk.accounts.TransferException;
import com.bnk.accounts.TransferService;
import com.bnk.accounts.Value;
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
            try {
                final Account fromAccount = accountsRepository.account(fromAccountId).orElseThrow(() -> new TransferException("Source account not found, id=" + fromAccountId));
                final Account toAccount = accountsRepository.account(toAccountId).orElseThrow(() -> new TransferException("Destination account not found id=" + toAccountId));
                synchronized(accountsRepository) {  
                    transferDelegate.transfer(fromAccount, toAccount, amount);
                }
            } catch (TransferException | NotAuhtorizedException ex) {
                Logger.getLogger(TransferServlet.class.getName()).log(Level.WARNING, ex.getMessage());
                response.getWriter().println("ERR:" + ex.getMessage());
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            } 
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("OK"); 
    }               
}
