package org.jboss.arquillian.mockserver.common.api;

import java.io.Serializable;

public class DeploymentInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String host;
    private int port;
    private int containerPort;
    private String context;
    
    public DeploymentInfo() {
        super();
    }
    
    public DeploymentInfo(String host, int port, String context, int containerPort) {
        super();
        this.host = host;
        this.port = port;
        this.context = context;
        this.containerPort = containerPort;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getContext() {
        return context;
    }
    
    public int getContainerPort() {
        return containerPort;
    }
    
}
