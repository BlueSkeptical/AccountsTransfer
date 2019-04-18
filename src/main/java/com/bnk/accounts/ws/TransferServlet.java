package com.bnk.accounts.ws;

import com.bnk.accounts.AccountNumber;
import com.bnk.accounts.AccountsRepository;
import com.bnk.accounts.TransferException;
import com.bnk.accounts.TransferService;
import com.bnk.accounts.Value;
import com.bnk.utils.fp.IO;
import com.bnk.utils.fp.Try;
import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TransferServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(TransferServlet.class.getName());
    
    private final AccountsRepository accountsRepository;
    private final TransferService transferService;
    
    public TransferServlet(AccountsRepository accountsRepository, TransferService transferSerivice) {
        this.accountsRepository = accountsRepository;
        this.transferService = transferSerivice;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {     
        parseParams(request)
        .flatMap(p -> transferService.transfer(p.fromAccountNumber, p.toAccountNumber, p.amount))
        .run(r -> write(response, r));
    }
   
    private static IO<Params> parseParams(HttpServletRequest request) {
       return IO.of(() -> new Params(new AccountNumber(Long.parseLong(request.getParameter(HttpClientTransferService.FROM_ACCOUNT_PARAMETER_NAME))),
                                     new AccountNumber(Long.parseLong(request.getParameter(HttpClientTransferService.TO_ACCOUNT_PARAMETER_NAME))),
                                     new Value(Long.parseLong(request.getParameter(HttpClientTransferService.AMOUNT_PARAMETER_NAME)))));
    }
    
    private static void write(HttpServletResponse response, Try<Integer> result) {
        result.onResult(r -> IO.effect(() -> { response.setContentType(HttpClientTransferService.PLAIN_TEXT_CONTENT_TYPE);
                                                response.setStatus(HttpServletResponse.SC_OK);
                                                write(response, r.toString()); }),
                     ex -> IO.effect(() -> { response.setContentType(HttpClientTransferService.PLAIN_TEXT_CONTENT_TYPE);
                                          response.setStatus(ex instanceof TransferException ? HttpServletResponse.SC_CONFLICT : HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
                                          write(response, "ERR:" + ex.getMessage());}));
    }
    
    
    private static void write(HttpServletResponse response, String str) throws IOException {
           response.getWriter().println(str);
    }
}