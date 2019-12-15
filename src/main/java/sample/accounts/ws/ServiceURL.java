package sample.accounts.ws;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.util.Objects;

public class ServiceURL {
    private final InetSocketAddress addr;
    private final String basePath;
    private final Param[] params;

    public ServiceURL(InetSocketAddress addr, String basePath, Param... params) {
        this.addr = Objects.requireNonNull(addr);
        this.basePath = Objects.requireNonNull(basePath);
        this.params = params;
    }

    public java.net.URL toURL() throws MalformedURLException {
       final StringBuilder sb = new StringBuilder();

       sb.append("http://");
       sb.append(addr.getHostString() + ":" + addr.getPort());
       sb.append("/" + basePath );
       for(int i= 0; i< params.length; i++) {
           sb.append(i==0 ? "?" : "&");
           sb.append(params[i].name + "=" + params[i].value.toString());
       }
       return new java.net.URL(sb.toString());
    }
}