package com.bnk.accounts.ws;

import com.bnk.accounts.Account;
import com.bnk.accounts.TransferException;
import com.bnk.accounts.TransferService;
import com.bnk.accounts.Value;
import com.bnk.utils.Result;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

/**
 * The transfer web service client
 */
public class HttpClientTransferService implements TransferService {

    public static final int READ_TIMEOUT_MS = 5_000;
    public static final int CONNECT_TIMEOUT_MS = 5_000;
    
    public static final String FROM_ACCOUNT_PARAMETER_NAME = "from";
    public static final String TO_ACCOUNT_PARAMETER_NAME = "to";
    public static final String AMOUNT_PARAMETER_NAME = "amount";
    public static final String CONTENT_TYPE = "text/plain";
    public static final String TRANSFER_RESOURCE_NAME = "transfer";
    public static final int BUSINESS_LOGIC_CONFLICT_HTTP_CODE = 409;
    
    private final InetSocketAddress address;
    private final String basePath;
    
    HttpClientTransferService(InetSocketAddress address, String basePath) {
       this.address = address;
       this.basePath = basePath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result<Result.Void> transfer(Account from, Account to, Value amount){
        try {
            final URL url = new URL( "http://" 
                                    + address.getHostString() + ":" + address.getPort()
                                    + basePath 
                                    + "/" + TRANSFER_RESOURCE_NAME
                                    + "?" + FROM_ACCOUNT_PARAMETER_NAME + "=" + from.id()
                                    + "&" + TO_ACCOUNT_PARAMETER_NAME + "=" + to.id()
                                    + "&" + AMOUNT_PARAMETER_NAME + "=" + amount);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
                
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", CONTENT_TYPE);
            con.setConnectTimeout(CONNECT_TIMEOUT_MS);
            con.setReadTimeout(READ_TIMEOUT_MS);
            
            if (con.getResponseCode() != 200) {
                final BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                final StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                      content.append(inputLine);
                }
                if(con.getResponseCode() == BUSINESS_LOGIC_CONFLICT_HTTP_CODE) {
                    return new Result.Fail(new TransferException(content.toString()));
                }
                return new Result.Fail(new RuntimeException("Server error: " + con.getResponseCode() + content));
            }
            return new Result.Success<>();
            
        } catch (IOException ex) {
             return new Result.Fail(new RuntimeException(ex));
        }
    }
    
}
