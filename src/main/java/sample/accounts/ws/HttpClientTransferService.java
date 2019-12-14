package sample.accounts.ws;

import sample.accounts.AccountNumber;
import sample.accounts.TransferException;
import sample.accounts.TransferService;
import sample.accounts.Value;
import lajkonik.fp.IO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * The transfer web service client
 */
public class HttpClientTransferService implements TransferService {

    public static final int READ_TIMEOUT_MS = 5_000;
    public static final int CONNECT_TIMEOUT_MS = 5_000;

    public static final String FROM_ACCOUNT_PARAMETER_NAME = "from";
    public static final String TO_ACCOUNT_PARAMETER_NAME = "to";
    public static final String AMOUNT_PARAMETER_NAME = "amount";
    public static final String PLAIN_TEXT_CONTENT_TYPE = "text/plain";
    public static final String TRANSFER_RESOURCE_NAME = "transfer";
    public static final int OK_HTTP_CODE = 200;
    public static final int BUSINESS_LOGIC_CONFLICT_HTTP_CODE = 409;

    private final InetSocketAddress address;
    private final String basePath;

    public HttpClientTransferService(InetSocketAddress address) {
        this(address, "");
    } 

    public HttpClientTransferService(InetSocketAddress address, String basePath) {
        this.address = Objects.requireNonNull(address);
        this.basePath = Objects.requireNonNull(basePath);
    }

    @Override
    public IO<Integer> transfer(AccountNumber from, AccountNumber to, Value amount) {
       return  IO.of(()-> transferImpl(from, to, amount));
    }
    
    
    public Integer transferImpl(AccountNumber from, AccountNumber to, Value amount) {
        try {
            final java.net.URL url = new ServiceURL(address,
                                                    basePath + "/" + TRANSFER_RESOURCE_NAME,
                                                    new ServiceURL.Param(FROM_ACCOUNT_PARAMETER_NAME, from),
                                                    new ServiceURL.Param(TO_ACCOUNT_PARAMETER_NAME, to),
                                                    new ServiceURL.Param(AMOUNT_PARAMETER_NAME, amount)).toURL();        
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", PLAIN_TEXT_CONTENT_TYPE);
            con.setConnectTimeout(CONNECT_TIMEOUT_MS);
            con.setReadTimeout(READ_TIMEOUT_MS);

            if (con.getResponseCode() != OK_HTTP_CODE) {
                final String responseErrorData = read(con.getErrorStream());
                final int responseCode = con.getResponseCode();
                if (responseCode == BUSINESS_LOGIC_CONFLICT_HTTP_CODE) {

                    throw new TransferException(responseErrorData);
                }
                throw new RuntimeException("Server error: " + responseCode + " " + responseErrorData);
            }
            final String responseData = read(con.getInputStream());
            return Integer.parseInt(responseData);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String read(InputStream is) throws IOException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String inputLine;
        final StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        return content.toString();
    }

}
