package org.jboss.arquillian.mockserver.moco.container.web;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.jboss.arquillian.mockserver.moco.client.MocoConfiguration;
import org.mockserver.integration.ClientAndServer;


@WebListener
public class MocoInitializer implements ServletContextListener {

    private MocoConfiguration mocoConfiguration;
    
    private ClientAndServer mockServer;
    
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        
        this.mocoConfiguration = getMocoConfiguration();
        
        mockServer = startClientAndServer(this.mocoConfiguration.getPort());
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        mockServer.stop();
    }

    private MocoConfiguration getMocoConfiguration() {

        Properties configuration = new Properties();
        try {
            configuration.load(MocoConfiguration.class.getResourceAsStream("/"
                    + MocoConfiguration.MOCO_PROPERTIES_FILENAME));
            return MocoConfiguration.fromProperties(configuration);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }
    

}
