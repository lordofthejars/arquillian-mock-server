package org.jboss.arquillian.mockserver.fakeftp.client;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.mockserver.common.ReflectionHelper;
import org.jboss.arquillian.mockserver.common.api.DeploymentInfo;
import org.jboss.arquillian.mockserver.fakeftp.container.FakeFtpRemoteExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class ArchiveAppender implements AuxiliaryArchiveAppender {

    @Override
    public Archive<?> createAuxiliaryArchive() {
        
        return ShrinkWrap.create(JavaArchive.class, "arquillian-fakeftp-extension.jar")
                .addPackage(ReflectionHelper.class.getPackage())
                .addClass(DeploymentInfo.class)
                .addAsServiceProvider(RemoteLoadableExtension.class, FakeFtpRemoteExtension.class)
                ;
        
    }

}
