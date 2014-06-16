package org.jboss.arquillian.mockserver.fakeftp.client;

import java.util.Map;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;

public class FakeFtpConfigurator {

    private static final String EXTENSION_NAME = "fakeftp";

    @Inject
    @ApplicationScoped
    private InstanceProducer<FakeFtpConfiguration> configurationProvider;

    public void configure(@Observes ArquillianDescriptor arquillianDescriptor) {
        Map<String, String> config = arquillianDescriptor.extension(EXTENSION_NAME).getExtensionProperties();
        configurationProvider.set(FakeFtpConfiguration.fromMap(config));
    }
    
}
