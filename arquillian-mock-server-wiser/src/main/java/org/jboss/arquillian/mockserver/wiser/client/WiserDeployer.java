package org.jboss.arquillian.mockserver.wiser.client;

import java.util.Collection;

import javax.servlet.ServletContextListener;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.protocol.metadata.HTTPContext;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.spi.event.container.AfterDeploy;
import org.jboss.arquillian.container.spi.event.container.AfterUnDeploy;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.mockserver.common.api.DeploymentInfo;
import org.jboss.arquillian.mockserver.wiser.WiserHostPortCommand;
import org.jboss.arquillian.mockserver.wiser.api.WiserResource;
import org.jboss.arquillian.mockserver.wiser.container.web.SentMessageServlet;
import org.jboss.arquillian.mockserver.wiser.container.web.WiserInitializer;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class WiserDeployer {

    private static final String WEBAPP_NAME = "arquillian-wiser.war";
    private static final String CONTEXT = "arquillian-wiser";

    private WebArchive deployedApplication;
    private ProtocolMetaData protocolMetaData;

    @Inject
    @ApplicationScoped
    InstanceProducer<DeploymentInfo> wiserHostPortProducer;
    
    @Inject
    Instance<WiserConfiguration> wiserConfiguration;

    public void wiserHostPort(@Observes WiserHostPortCommand wiserHostPortEvent) {
        wiserHostPortEvent.setResult(wiserHostPortProducer.get());
    }

    public void executeBeforeDeploy(@Observes AfterDeploy event) throws DeploymentException {
        JavaArchive[] wiserDependencies = Maven.resolver().loadPomFromFile("pom.xml")
                .resolve(ArchiveProcessor.WISER_ARTIFACT).withTransitivity().as(JavaArchive.class);

        this.deployedApplication = resolveMockServerArchive(
                org.jboss.arquillian.mockserver.wiser.container.web.WiserInitializer.class, wiserDependencies);

        DeployableContainer<?> deployableContainer = event.getDeployableContainer();
        this.protocolMetaData = deployableContainer.deploy(this.deployedApplication);

        Collection<HTTPContext> contexts = this.protocolMetaData.getContexts(HTTPContext.class);

        if (contexts != null && contexts.size() > 0) {

            HTTPContext httpContext = contexts.iterator().next();
            DeploymentInfo wiserHostPort = new DeploymentInfo(httpContext.getHost(),
                    wiserConfiguration.get().getPort(), CONTEXT,
                    httpContext.getPort());
            this.wiserHostPortProducer.set(wiserHostPort);

        }

    }

    public void executeAfterUnDeploy(@Observes AfterUnDeploy event) throws DeploymentException {

        DeployableContainer<?> deployableContainer = event.getDeployableContainer();
        deployableContainer.undeploy(this.deployedApplication);

    }

    private WebArchive resolveMockServerArchive(Class<? extends ServletContextListener> initializer,
            JavaArchive... libs) {
        
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, WEBAPP_NAME).addClass(WiserInitializer.class)
                .addClass(SentMessageServlet.class).addClass(WiserResource.class).addClass(WiserConfiguration.class).addAsLibraries(libs);
        
        webArchive.addAsResource(new StringAsset(wiserConfiguration.get().toProperties()), WiserConfiguration.WISER_PROPERTIES_FILENAME);
        
        return webArchive;
    }

}
