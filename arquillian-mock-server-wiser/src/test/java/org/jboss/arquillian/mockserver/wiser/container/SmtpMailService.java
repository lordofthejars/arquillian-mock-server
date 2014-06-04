package org.jboss.arquillian.mockserver.wiser.container;

import java.util.Properties;

import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
public class SmtpMailService {

    public void sendEmail(String host, int port) {
        
        Properties properties = new Properties();
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.host", host);

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
            throw new IllegalArgumentException(mex);
        }
        
    }
    
}
