
package com.bnk.accounts.ws;

import com.bnk.accounts.TransferException;
import com.bnk.accounts.TransferService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TransferServlet extends HttpServlet {
    private final TransferService transferDelegate;
    
    public TransferServlet(TransferService transferDelegate) {
        this.transferDelegate = transferDelegate;
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(HttpClientTransferService.CONTENT_TYPE);
        
        final int fromAccountId = Integer.parseInt(request.getParameter(HttpClientTransferService.FROM_ACCOUNT_PARAMETER_NAME));
        final int toAccountId = Integer.parseInt(request.getParameter(HttpClientTransferService.TO_ACCOUNT_PARAMETER_NAME));
        final long amount = Long.parseLong(request.getParameter(HttpClientTransferService.AMOUNT_PARAMETER_NAME));
        try {
            transferDelegate.transfer(fromAccountId, toAccountId, amount);
        } catch (TransferException ex) {
            Logger.getLogger(TransferServlet.class.getName()).log(Level.WARNING, ex.getMessage());
            response.getWriter().println("ERR:" + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("OK");
    }
}
