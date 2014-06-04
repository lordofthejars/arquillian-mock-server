package org.jboss.arquillian.mockserver.wiser.client;

import java.util.Map;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;

public class WiserConfigurator {

    private static final String EXTENSION_NAME = "wiser";

    @Inject
    @ApplicationScoped
    private InstanceProducer<WiserConfiguration> configurationProvider;

    public void configure(@Observes ArquillianDescriptor arquillianDescriptor) {
        Map<String, String> config = arquillianDescriptor.extension(EXTENSION_NAME).getExtensionProperties();
        configurationProvider.set(WiserConfiguration.fromMap(config));
    }

}
