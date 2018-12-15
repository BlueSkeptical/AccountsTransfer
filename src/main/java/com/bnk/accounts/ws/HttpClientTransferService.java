package com.bnk.accounts.ws;

import com.bnk.accounts.TransferException;
import com.bnk.accounts.TransferService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpClientTransferService implements TransferService {

    public static final int READ_TIMEOUT = 5_000;
    public static final int CONNECT_TIMEOUNT = 5_000;
    
    private final InetSocketAddress address;
    
    HttpClientTransferService(InetSocketAddress address) {
       this.address = address;
    }

    @Override
    public void transfer(int from, int to, long amount) throws TransferException {
        try {
            final URL url = new URL( "http://" 
                                    + address.getHostString() + ":" + address.getPort()
                                    + "/accounts/transfer?from=" + from
                                    + "&to=" + to 
                                    + "&amount=" + amount);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
                
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "text/plain");
            con.setConnectTimeout(CONNECT_TIMEOUNT);
            con.setReadTimeout(READ_TIMEOUT);
            
            final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            final StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                  content.append(inputLine);
            }
            
            if( con.getResponseCode() != 200)
            {
                throw new TransferException(content.toString());
            }
            
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        } catch(IOException ex) {
            throw new TransferException();
        }
    }
    
}
