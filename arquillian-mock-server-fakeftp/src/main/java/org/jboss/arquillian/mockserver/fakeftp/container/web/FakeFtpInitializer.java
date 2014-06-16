package org.jboss.arquillian.mockserver.fakeftp.container.web;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.jboss.arquillian.mockserver.fakeftp.client.FakeFtpConfiguration;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.WindowsFakeFileSystem;

@WebListener
public class FakeFtpInitializer implements ServletContextListener {

    private FakeFtpServer fakeFtpServer;
    private FakeFtpConfiguration fakeFtpConfiguration;
    
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
       
        this.fakeFtpConfiguration = getFakeFtpConfiguration();
        
        this.fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.addUserAccount(new UserAccount("user", "password", "C:\\data"));
        
        FileSystem fileSystem = new WindowsFakeFileSystem();
        fileSystem.add(new DirectoryEntry("c:\\data"));

        this.fakeFtpServer.setFileSystem(fileSystem);
        
        this.fakeFtpServer.setServerControlPort(fakeFtpConfiguration.getPort());
        this.fakeFtpServer.start();
        
    }

    
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        
        if(this.fakeFtpServer != null) {
            this.fakeFtpServer.stop();
        }
        
    }

    private FakeFtpConfiguration getFakeFtpConfiguration() {

        Properties configuration = new Properties();
        try {
            configuration.load(FakeFtpInitializer.class.getResourceAsStream("/"
                    + FakeFtpConfiguration.FAKEFTP_PROPERTIES_FILENAME));
            return FakeFtpConfiguration.fromProperties(configuration);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }
    
    
}
