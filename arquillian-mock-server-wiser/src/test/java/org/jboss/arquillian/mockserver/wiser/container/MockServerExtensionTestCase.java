package org.jboss.arquillian.mockserver.wiser.container;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.mockserver.common.api.DeploymentInfo;
import org.jboss.arquillian.mockserver.wiser.api.WiserMessages;
import org.jboss.arquillian.mockserver.wiser.api.WiserEmailMessage;
import org.jboss.arquillian.mockserver.wiser.api.WiserConnection;
import org.jboss.arquillian.mockserver.wiser.api.WiserResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MockServerExtensionTestCase {

    @Deployment(testable = true)
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class, "myapp.war").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @WiserResource
    DeploymentInfo wiserHostPort;

    @WiserConnection
    WiserMessages wiserConnection;
    
    @EJB
    SmtpMailService smtpMailService;
    
    @Test
    public void should_send_email_to_wiser() throws IOException {

        smtpMailService.sendEmail(wiserHostPort.getHost(), wiserHostPort.getPort());

        List<WiserEmailMessage> messages = wiserConnection.messages();
        WiserEmailMessage message = messages.get(0);
        
        assertThat(message.getEnvelopeReceiver(), is("bb@bb.es"));
        assertThat(message.getEnvelopeSender(), is("aa@aa.es"));
        assertThat(new String(message.getMessage()), containsString("Subject: This is the Subject Line!"));
        assertThat(new String(message.getMessage()), containsString("This is actual message"));
        
    }
}
