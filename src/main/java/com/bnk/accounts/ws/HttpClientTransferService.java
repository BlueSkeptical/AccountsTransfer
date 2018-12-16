package com.bnk.accounts.ws;

import com.bnk.accounts.Account;
import com.bnk.accounts.TransferException;
import com.bnk.accounts.TransferService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

/**
 * The transfers web service client
 * for simple HTTP call
 */
public class HttpClientTransferService implements TransferService {

    public static final int READ_TIMEOUT = 5_000;
    public static final int CONNECT_TIMEOUT = 5_000;
    
    public static final String TO_ACCOUNT_PARAMETER_NAME = "to";
    public static final String AMOUNT_PARAMETER_NAME = "amount";
    public static final String CONTENT_TYPE = "text/plain";
    public static final String ACCOUNTS_API_PATH = "accounts";
    public static final String TRANSFER_API_PATH = "transfer";
    public static final String HTTP_METHOD = "POST";
    public static final int BUSINESS_LOGIC_CONFLICT_HTTP_CODE = 409;
    
    private final InetSocketAddress address;
    
    HttpClientTransferService(InetSocketAddress address) {
       this.address = address;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void transfer(Account from, Account to, long amount) throws TransferException {
        try {
            final URL url = new URL( "http://" 
                                    + address.getHostString() + ":" + address.getPort()
                                    + "/" + ACCOUNTS_API_PATH 
                                    + "/" + from.id()
                                    + "/" + TRANSFER_API_PATH
                                    + "?" + TO_ACCOUNT_PARAMETER_NAME + "=" + to.id()
                                    + "&" + AMOUNT_PARAMETER_NAME + "=" + amount);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
                
            con.setRequestMethod(HTTP_METHOD);
            con.setRequestProperty("Content-Type", CONTENT_TYPE);
            con.setConnectTimeout(CONNECT_TIMEOUT);
            con.setReadTimeout(READ_TIMEOUT);
            
            if (con.getResponseCode() != 200) {
                final BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                final StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                      content.append(inputLine);
                }
                if(con.getResponseCode() == BUSINESS_LOGIC_CONFLICT_HTTP_CODE) {
                    throw new TransferException(content.toString());
                }
                throw new RuntimeException("Server error: " + con.getResponseCode() + content);
            }
            
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
