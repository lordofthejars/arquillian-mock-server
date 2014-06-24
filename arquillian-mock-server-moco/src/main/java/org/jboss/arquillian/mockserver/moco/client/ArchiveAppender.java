package org.jboss.arquillian.mockserver.moco.client;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.mockserver.common.ReflectionHelper;
import org.jboss.arquillian.mockserver.moco.api.HostPortCommand;
import org.jboss.arquillian.mockserver.moco.container.MocoRemoteExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class ArchiveAppender implements AuxiliaryArchiveAppender {

    @Override
    public Archive<?> createAuxiliaryArchive() {
        
        return ShrinkWrap.create(JavaArchive.class, "arquillian-moco-extension.jar")
                .addPackage(ReflectionHelper.class.getPackage())
                .addClass(MocoConfiguration.class)
                .addClass(MocoConfigurator.class)
                .addPackages(true, HostPortCommand.class.getPackage())
                .addAsServiceProvider(RemoteLoadableExtension.class, MocoRemoteExtension.class)
                ;
        
    }

}
