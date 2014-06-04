package org.jboss.arquillian.mockserver.wiser.client;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.mockserver.common.api.DeploymentInfo;
import org.jboss.arquillian.mockserver.wiser.api.WiserEmailMessage;
import org.jboss.arquillian.mockserver.wiser.api.WiserConnection;
import org.jboss.arquillian.mockserver.wiser.api.WiserMessages;
import org.jboss.arquillian.mockserver.wiser.api.WiserResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MockServerExtensionTestCase {

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class, "myapp.war").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @WiserResource
    DeploymentInfo wiserHostPort;

    @WiserMessages
    WiserConnection wiserConnection;
    
    @Test
    public void should_send_email_to_wiser() throws IOException {

        sentEmail();
        List<WiserEmailMessage> messages = wiserConnection.messages();
        
        WiserEmailMessage message = messages.get(0);
        
        assertThat(message.getEnvelopeReceiver(), is("bb@bb.es"));
        assertThat(message.getEnvelopeSender(), is("aa@aa.es"));
        assertThat(new String(message.getMessage()), containsString("Subject: This is the Subject Line!"));
        assertThat(new String(message.getMessage()), containsString("This is actual message"));
        
    }

    private void sentEmail() {
        Properties properties = new Properties();
        properties.put("mail.smtp.port", wiserHostPort.getPort());
        properties.put("mail.smtp.host", wiserHostPort.getHost());

        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress("aa@aa.es"));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("bb@bb.es"));

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            // Now set the actual message
            message.setText("This is actual message");

            // Send message
            Transport.send(message);

        } catch (MessagingException mex) {
            fail();
        }
    }
}
