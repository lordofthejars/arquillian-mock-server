package org.jboss.arquillian.mockserver.wiser.container.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.subethamail.wiser.Wiser;

@WebListener
public class WiserInitializer implements ServletContextListener {

    public static final int WISER_DEFAULT_PORT = 2500;
    
    private Wiser wiser;
    
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        this.wiser.stop();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        this.wiser = new Wiser();
        wiser.setPort(WISER_DEFAULT_PORT);
        wiser.start();
        
        event.getServletContext().setAttribute("wiser", this.wiser);
        
    }

}
