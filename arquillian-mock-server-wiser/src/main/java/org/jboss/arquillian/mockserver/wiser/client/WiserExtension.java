package org.jboss.arquillian.mockserver.wiser.client;

import org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor;
import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.test.spi.TestEnricher;

public class WiserExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder)   {
        
        builder.service(AuxiliaryArchiveAppender.class, ArchiveAppender.class);
        builder.service(ApplicationArchiveProcessor.class, ArchiveProcessor.class);
        
        builder.observer(WiserDeployer.class);
        builder.service(TestEnricher.class, WiserResourceTestEnricher.class);
        builder.service(TestEnricher.class, WiserMessagesTestEnricher.class);
    }

}
