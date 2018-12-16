
package com.bnk.accounts.ws;

import com.bnk.accounts.Account;
import com.bnk.accounts.AccountsRepository;
import com.bnk.accounts.TransferException;
import com.bnk.accounts.TransferService;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AccountsServlet extends HttpServlet {
    private final AccountsRepository accountsRepository;
    private final TransferService transferDelegate;
    
    public AccountsServlet(AccountsRepository accountsRepository, TransferService transferDelegate) {
        this.accountsRepository = accountsRepository;
        this.transferDelegate = transferDelegate;
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(HttpClientTransferService.CONTENT_TYPE);
        final List<String> path = splitPath(request.getPathInfo());
        if (path.size() > 0 && isInteger(path.get(0))) {
            final int fromAccountId = Integer.parseInt(path.get(0));
            if (path.size() > 1 && HttpClientTransferService.TRANSFER_API_PATH.equals(path.get(1))) {
                final int toAccountId = Integer.parseInt(request.getParameter(HttpClientTransferService.TO_ACCOUNT_PARAMETER_NAME));
                final long amount = Long.parseLong(request.getParameter(HttpClientTransferService.AMOUNT_PARAMETER_NAME));
                try {
                    final Account fromAccount = accountsRepository.account(fromAccountId).orElseThrow(() -> new TransferException("Source account not found, id=" + fromAccountId));
                    final Account toAccount = accountsRepository.account(toAccountId).orElseThrow(() -> new TransferException("Destination account not found id=" + toAccountId));
                    transferDelegate.transfer(fromAccount, toAccount, amount);
                } catch (TransferException ex) {
                    Logger.getLogger(AccountsServlet.class.getName()).log(Level.WARNING, ex.getMessage());
                    response.getWriter().println("ERR:" + ex.getMessage());
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    return;
                }
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("OK"); 
                return;
            }            
        }        
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().println("Not supported");   
    }
    
    private List<String> splitPath(String path) {
        return Stream.of(path.split("/")).filter( p -> !"".equals(p)).collect(Collectors.toList());
    }
    
    private boolean isInteger(String str) {
        return str.matches("-?\\d+");
    }
}
