package org.jboss.arquillian.mockserver.wiser.client;

import org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.container.LibraryContainer;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class ArchiveProcessor implements ApplicationArchiveProcessor {

    public static final String WISER_ARTIFACT = "org.subethamail:subethasmtp:";
    
    @Override
    public void process(Archive<?> applicationArchive, TestClass testClass) {

        if(applicationArchive instanceof LibraryContainer) {
            
            LibraryContainer<?> container = (LibraryContainer<?>) applicationArchive;
            container.addAsLibraries(
                    Maven.resolver().loadPomFromFile("pom.xml")
                    .resolve(WISER_ARTIFACT)
                    .withTransitivity()
                    .asFile());
            
        }
        
    }

}
