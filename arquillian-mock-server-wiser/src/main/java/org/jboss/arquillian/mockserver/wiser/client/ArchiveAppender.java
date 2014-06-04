package org.jboss.arquillian.mockserver.wiser.client;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.mockserver.common.ReflectionHelper;
import org.jboss.arquillian.mockserver.common.api.DeploymentInfo;
import org.jboss.arquillian.mockserver.wiser.WiserHostPortCommand;
import org.jboss.arquillian.mockserver.wiser.api.WiserResource;
import org.jboss.arquillian.mockserver.wiser.container.WiserRemoteExtension;
import org.jboss.arquillian.mockserver.wiser.container.WiserResourceTestEnricherByCommand;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class ArchiveAppender implements AuxiliaryArchiveAppender {

    @Override
    public Archive<?> createAuxiliaryArchive() {
        
        return ShrinkWrap.create(JavaArchive.class, "arquillian-wiser-extension.jar")
                .addPackage(ReflectionHelper.class.getPackage())
                .addClass(WiserHostPortCommand.class)
                .addClass(WiserResourceTestEnricherByCommand.class)
                .addClass(WiserRemoteExtension.class)
                .addClass(WiserResource.class)
                .addClass(WiserMessagesTestEnricher.class)
                .addPackage(DeploymentInfo.class.getPackage())
                .addAsServiceProvider(RemoteLoadableExtension.class, WiserRemoteExtension.class)
                ;
        
    }

}
