package org.jboss.arquillian.mockserver.wiser.container;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.mockserver.wiser.client.WiserMessagesTestEnricher;
import org.jboss.arquillian.test.spi.TestEnricher;

public class WiserRemoteExtension implements RemoteLoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
       builder.service(TestEnricher.class, WiserResourceTestEnricherByCommand.class);
       builder.service(TestEnricher.class, WiserMessagesTestEnricher.class);
    }

}
