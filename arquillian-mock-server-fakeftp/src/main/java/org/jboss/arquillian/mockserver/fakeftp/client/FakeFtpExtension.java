package org.jboss.arquillian.mockserver.fakeftp.client;

import org.jboss.arquillian.core.spi.LoadableExtension;

public class FakeFtpExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        
        builder.observer(FakeFtpDeployer.class);
        builder.observer(FakeFtpConfigurator.class);

    }

}
