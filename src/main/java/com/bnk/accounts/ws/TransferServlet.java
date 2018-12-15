
package com.bnk.accounts.ws;

import com.bnk.accounts.AccountsRepository;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TransferServlet extends HttpServlet {
    private final AccountsRepository accountsRepository;
    
    public TransferServlet(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("hello world");
    }
}
