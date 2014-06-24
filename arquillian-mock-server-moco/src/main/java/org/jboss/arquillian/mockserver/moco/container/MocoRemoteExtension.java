package org.jboss.arquillian.mockserver.moco.container;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;

public class MocoRemoteExtension implements RemoteLoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
       //builder.service(TestEnricher.class, WiserResourceTestEnricherByCommand.class);
       //builder.service(TestEnricher.class, WiserMessagesTestEnricher.class);
    }

}
