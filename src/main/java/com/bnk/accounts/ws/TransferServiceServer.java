/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bnk.accounts.ws;

import com.bnk.accounts.AccountsRepository;
import com.bnk.accounts.SimpleAccountsRepository;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 *
 * @author ThinkPad
 */
public class TransferServiceServer {

    public static final int DEFAULT_PORT = 8080;
    private final AccountsRepository accountsRepository;
    private final int port;
    private Server server;
    
    public TransferServiceServer(int port, AccountsRepository accountsRepository) {
        this.port = port;
        this.accountsRepository = accountsRepository;
    }

    public void start() throws Exception {
        server = new Server();
        
        final ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[] {connector});
        
        final ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/accounts"); 
        
        context.addServlet(new ServletHolder(new TransferServlet(accountsRepository)), "/transfer/*");
        
        server.setHandler(context);
        server.start(); 
        
        while( !server.isRunning()) {
            Thread.sleep(500);
        }    
    }
    
    public void join() throws InterruptedException {
        server.join();
    }
    
    public static void main(String[] args) throws Exception {
        final int p = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        final TransferServiceServer s = new TransferServiceServer(p, new SimpleAccountsRepository()); //TODO implement an accounts repository reading from some persistent store, e.g. a file
        s.start();
        s.join();
    }
}
