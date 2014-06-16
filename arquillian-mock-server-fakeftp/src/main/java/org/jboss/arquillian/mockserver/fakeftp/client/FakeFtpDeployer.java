package org.jboss.arquillian.mockserver.fakeftp.client;

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
import org.jboss.arquillian.mockserver.fakeftp.api.HostPortCommand;
import org.jboss.arquillian.mockserver.fakeftp.container.web.FakeFtpInitializer;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class FakeFtpDeployer {

    private static final String WEBAPP_NAME = "arquillian-fakeftp.war";
    private static final String CONTEXT = "arquillian-fakeftp";

    private WebArchive deployedApplication;
    private ProtocolMetaData protocolMetaData;

    @Inject
    @ApplicationScoped
    InstanceProducer<DeploymentInfo> fakeFtpHostPortProducer;
    
    @Inject
    Instance<FakeFtpConfiguration> fakeFtpConfiguration;
    
    public void fakeFtpHostPort(@Observes HostPortCommand fakeftpHostPortEvent) {
        fakeftpHostPortEvent.setResult(fakeFtpHostPortProducer.get());
    }

    public void executeBeforeDeploy(@Observes AfterDeploy event) throws DeploymentException {
        JavaArchive[] fakeFpDependencies = Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("org.mockftpserver:MockFtpServer").withTransitivity().as(JavaArchive.class);

        this.deployedApplication = resolveMockServerArchive(
                FakeFtpInitializer.class, fakeFpDependencies);

        DeployableContainer<?> deployableContainer = event.getDeployableContainer();
        this.protocolMetaData = deployableContainer.deploy(this.deployedApplication);

        Collection<HTTPContext> contexts = this.protocolMetaData.getContexts(HTTPContext.class);

        if (contexts != null && contexts.size() > 0) {

            HTTPContext httpContext = contexts.iterator().next();
            DeploymentInfo fakeFtpHostPort = new DeploymentInfo(httpContext.getHost(),
                    this.fakeFtpConfiguration.get().getPort(), CONTEXT,
                    httpContext.getPort());
            this.fakeFtpHostPortProducer.set(fakeFtpHostPort);

        }

    }

    public void executeAfterUnDeploy(@Observes AfterUnDeploy event) throws DeploymentException {

        DeployableContainer<?> deployableContainer = event.getDeployableContainer();
        
        if(this.deployedApplication != null) {
            deployableContainer.undeploy(this.deployedApplication);
        }
    }

    private WebArchive resolveMockServerArchive(Class<? extends ServletContextListener> initializer,
            JavaArchive... libs) {
        
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, WEBAPP_NAME).addClass(initializer).addClass(FakeFtpConfiguration.class).addAsLibraries(libs);
        
        FakeFtpConfiguration configuration = fakeFtpConfiguration.get();
        if(configuration != null) {
            webArchive.addAsResource(new StringAsset(configuration.toProperties()), FakeFtpConfiguration.FAKEFTP_PROPERTIES_FILENAME);
        }
        
        return webArchive;
    }
    
}
