package com.bnk.accounts.ws;

import com.bnk.accounts.AccountNumber;
import com.bnk.accounts.AccountsRepository;
import com.bnk.accounts.TransferService;
import com.bnk.accounts.Value;
import com.bnk.utils.fp.IO;
import com.bnk.utils.fp.Nothing;
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
        parseParams(request).io(p -> transfer(p, response),
                                      ex -> IO.effect(() -> {
                                          response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                          write(response, "ERR:" + ex.getMessage());       
                                         }));
    }
    
    private IO<Nothing> transfer(Params p, HttpServletResponse response) {
        return IO.effect(() -> { transferService.transfer(p.fromAccountNumber, p.toAccountNumber, p.amount)
                                                .onCallback(r -> write(response, r));
                               });
    }
    
    private static void write(HttpServletResponse response, Try<Integer> result) {
        result.io(r -> IO.effect(() -> { response.setStatus(HttpServletResponse.SC_OK);
                                         write(response, r.toString()); }),
                  ex -> IO.effect(() -> { response.setStatus(HttpServletResponse.SC_CONFLICT); 
                                          write(response, "ERR:" + ex.getMessage());}));
    }
    
    
    private static Try<Params> parseParams(HttpServletRequest request) {
       return Try.of(() -> new Params(new AccountNumber(Long.parseLong(request.getParameter(HttpClientTransferService.FROM_ACCOUNT_PARAMETER_NAME))),
                               new AccountNumber(Long.parseLong(request.getParameter(HttpClientTransferService.TO_ACCOUNT_PARAMETER_NAME))),
                               new Value(Long.parseLong(request.getParameter(HttpClientTransferService.AMOUNT_PARAMETER_NAME)))));
     
    }
    
    
    private static void write(HttpServletResponse response, String str) {
        try {
            response.getWriter().println(str);
        } catch (IOException ex) {
            Logger.getLogger(TransferServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
