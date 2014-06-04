package org.jboss.arquillian.mockserver.wiser.container.web;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.jboss.arquillian.mockserver.wiser.client.WiserConfiguration;
import org.subethamail.wiser.Wiser;

@WebListener
public class WiserInitializer implements ServletContextListener {

    private WiserConfiguration wiserConfiguration;
    private Wiser wiser;

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        this.wiser.stop();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        
        this.wiserConfiguration = getWiserConfiguration();
        this.wiser = new Wiser();
        
        wiser.setPort(wiserConfiguration.getPort());
        wiser.start();

        event.getServletContext().setAttribute("wiser", this.wiser);

    }

    private WiserConfiguration getWiserConfiguration() {

        Properties configuration = new Properties();
        try {
            configuration.load(WiserInitializer.class.getResourceAsStream("/"
                    + WiserConfiguration.WISER_PROPERTIES_FILENAME));
            return WiserConfiguration.fromProperties(configuration);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }

}
