package com.bnk.accounts.ws;

import com.bnk.accounts.AccountNumber;
import com.bnk.accounts.AccountsRepository;
import com.bnk.accounts.TransferService;
import com.bnk.accounts.Value;
import com.bnk.utils.fp.Try;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TransferServlet extends HttpServlet {

    private final AccountsRepository accountsRepository;
    private final TransferService transferService;

    public TransferServlet(AccountsRepository accountsRepository, TransferService transferSerivice) {
        this.accountsRepository = accountsRepository;
        this.transferService = transferSerivice;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(HttpClientTransferService.CONTENT_TYPE);
        final int fromAccountId = Integer.parseInt(request.getParameter(HttpClientTransferService.FROM_ACCOUNT_PARAMETER_NAME));
        final int toAccountId = Integer.parseInt(request.getParameter(HttpClientTransferService.TO_ACCOUNT_PARAMETER_NAME));
        final Value amount = new Value(Long.parseLong(request.getParameter(HttpClientTransferService.AMOUNT_PARAMETER_NAME)));
        synchronized (accountsRepository) {
            transfer(new AccountNumber(fromAccountId), new AccountNumber(toAccountId), amount, response);
        }
    }

    private void transfer(AccountNumber from, AccountNumber to, Value amount, HttpServletResponse response) {
        transferService.transfer(from, to, amount)
                        .io(p -> { response.setStatus(HttpServletResponse.SC_OK);
                                      write(response, p.toString());
                                      return null; }, //TODO
                               p -> { response.setStatus(HttpServletResponse.SC_CONFLICT);
                                      write(response, "ERR:" + p.getMessage());
                                      Logger.getLogger(TransferServlet.class.getName()).log(Level.WARNING, p.getMessage());
                                      return null; } );
   
    }
    
    private static void write(HttpServletResponse response, String str) {
        try {
            response.getWriter().println(str);
        } catch (IOException ex) {
            Logger.getLogger(TransferServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
