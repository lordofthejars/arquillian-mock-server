package org.jboss.arquillian.mockserver.moco.client;

import java.util.Collection;

import javax.servlet.ServletContextListener;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.protocol.metadata.HTTPContext;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.spi.event.container.AfterDeploy;
import org.jboss.arquillian.container.spi.event.container.AfterUnDeploy;
import org.jboss.arquillian.container.spi.event.container.BeforeStop;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.mockserver.common.api.DeploymentInfo;
import org.jboss.arquillian.mockserver.moco.api.HostPortCommand;
import org.jboss.arquillian.mockserver.moco.container.web.MocoInitializer;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependencies;

public class MocoDeployer {

    private static final String WEBAPP_NAME = "arquillian-moco.war";
    private static final String CONTEXT = "arquillian-moco";

    private WebArchive deployedApplication;
    private ProtocolMetaData protocolMetaData;

    @Inject
    @ApplicationScoped
    InstanceProducer<DeploymentInfo> mocoHostPortProducer;
    
    @Inject
    Instance<MocoConfiguration> mocoConfiguration;

    public void mcoHostPort(@Observes HostPortCommand wiserHostPortEvent) {
        wiserHostPortEvent.setResult(mocoHostPortProducer.get());
    }

    public void executeBeforeDeploy(@Observes AfterDeploy event) throws DeploymentException {
        

        this.deployedApplication = resolveMockServerArchive(
                MocoInitializer.class);

        DeployableContainer<?> deployableContainer = event.getDeployableContainer();
        this.protocolMetaData = deployableContainer.deploy(this.deployedApplication);

        Collection<HTTPContext> contexts = this.protocolMetaData.getContexts(HTTPContext.class);

        if (contexts != null && contexts.size() > 0) {

            HTTPContext httpContext = contexts.iterator().next();
            DeploymentInfo mocoHostPort = new DeploymentInfo(httpContext.getHost(),
                    mocoConfiguration.get().getPort(), CONTEXT,
                    httpContext.getPort());
            this.mocoHostPortProducer.set(mocoHostPort);

        }

    }

    public void executeAfterUnDeploy(@Observes BeforeStop event) throws DeploymentException {

        DeployableContainer<?> deployableContainer = event.getDeployableContainer();
        deployableContainer.undeploy(this.deployedApplication);

    }

    private WebArchive resolveMockServerArchive(Class<? extends ServletContextListener> initializer,
            JavaArchive... libs) {
        
        WebArchive mockServer = Maven.configureResolver().withRemoteRepo("sonatype-nexus-snapshots", "https://oss.sonatype.org/content/repositories/snapshots", "default")
                .resolve("org.mock-server:mockserver-war:war:3.1-SNAPSHOT").withoutTransitivity().asSingle(WebArchive.class);
        
        return mockServer;
        
        /*WebArchive webArchive = ShrinkWrap.create(WebArchive.class, WEBAPP_NAME).addClass(initializer).addClass(MocoConfiguration.class).addAsLibraries(libs);
        
        webArchive.addAsResource(new StringAsset(mocoConfiguration.get().toProperties()), MocoConfiguration.MOCO_PROPERTIES_FILENAME);
        
        return webArchive;*/
    }
    
    
}
