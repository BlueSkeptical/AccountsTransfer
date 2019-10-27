package sample.accounts.ws;

import sample.accounts.Account;
import sample.accounts.AccountNumber;
import sample.accounts.AccountsRepository;
import sample.accounts.DefaultTransferService;
import sample.accounts.OwnerName;
import sample.accounts.SimpleAccountsRepository;
import sample.accounts.TransferService;
import sample.accounts.Value;
import java.util.ArrayList;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 * The web server 
 */
public class TransferServiceServer {

    public static final int DEFAULT_PORT = 8080;
    private static final int MAX_THREADS = 50;
    
    private final TransferService transferService;
    private final int port;
    private final String basePath;
    
    private Server server;
    
    public TransferServiceServer(int port, String basePath, TransferService transferService) {
        this.port = port;
        this.basePath = basePath;
        this.transferService = transferService;
    }

    public void start() throws Exception {
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(MAX_THREADS);
        
        server = new Server(threadPool);
        
        final ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[] {connector});
        
        final ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/" + basePath); 
        
        context.addServlet(new ServletHolder(new TransferServlet(transferService)),
                                             "/*");
        
        server.setHandler(context);
        server.start();    
    }
    
    public void join() throws InterruptedException {
        server.join();
    }
    
    public static void main(String[] args) throws Exception {
        final int p = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        final AccountsRepository ar = new SimpleAccountsRepository(new ArrayList<>(),
                                                                   Account.snapshot(new AccountNumber(10001), new OwnerName("Joe", "Doe"), new Value(1000)),
                                                                   Account.snapshot(new AccountNumber(10002), new OwnerName("Mary", "Smith"), new Value(0)),
                                                                   Account.snapshot(new AccountNumber(10003), new OwnerName("Jan", "Kowalksi"), new Value(2000)) );
        final TransferService ts = new DefaultTransferService(ar);
        final TransferServiceServer s = new TransferServiceServer(p, "", ts); //TODO implement an accounts repository reading from some persistent store, e.g. a file
        s.start();
        s.join();
    }
}
