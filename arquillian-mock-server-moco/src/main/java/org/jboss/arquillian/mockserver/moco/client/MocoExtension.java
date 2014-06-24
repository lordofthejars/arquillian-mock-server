package org.jboss.arquillian.mockserver.moco.client;

import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.core.spi.LoadableExtension;

public class MocoExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {

        builder.service(AuxiliaryArchiveAppender.class, ArchiveAppender.class);
        builder.observer(MocoDeployer.class);
        builder.observer(MocoConfigurator.class);
        
    }

    
    
}
