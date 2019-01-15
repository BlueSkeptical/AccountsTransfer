package com.bnk.accounts.ws;

import com.bnk.accounts.AccountsRepository;
import com.bnk.accounts.DefaultAccount;
import com.bnk.accounts.DefaultTransferService;
import com.bnk.accounts.OwnerName;
import com.bnk.accounts.SimpleAccountsRepository;
import com.bnk.accounts.TransferService;
import com.bnk.accounts.Value;
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
    
    private final AccountsRepository accountsRepository;
    private final TransferService transferService;
    private final int port;
    private final String basePath;
    
    private Server server;
    
    public TransferServiceServer(int port, String basePath, AccountsRepository accountsRepository, TransferService transferService) {
        this.port = port;
        this.basePath = basePath;
        this.accountsRepository = accountsRepository;
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
        
        context.addServlet(new ServletHolder(new TransferServlet(accountsRepository, transferService)),
                                             "/*");
        
        server.setHandler(context);
        server.start();    
    }
    
    public void join() throws InterruptedException {
        server.join();
    }
    
    public static void main(String[] args) throws Exception {
        final int p = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        final TransferService ts = new DefaultTransferService();
        final AccountsRepository ar = new SimpleAccountsRepository(new DefaultAccount(10001, new OwnerName("Joe", "Doe"), new Value(1000), ts),
                                                                   new DefaultAccount(10002, new OwnerName("Mary", "Smith"), new Value(0), ts),
                                                                   new DefaultAccount(10003, new OwnerName("Jan", "Kowalksi"), new Value(2000), ts) );
        
        final TransferServiceServer s = new TransferServiceServer(p, "",  ar, ts); //TODO implement an accounts repository reading from some persistent store, e.g. a file
        s.start();
        s.join();
    }
}
