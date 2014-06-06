package org.jboss.arquillian.mockserver.wiser.api;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import javax.json.JsonObject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.subethamail.smtp.util.Base64;

public class WiserEmailMessage {

    private String envelopeSender;
    private String envelopeReceiver;
    
    private Message message;

    public WiserEmailMessage(String envelopeSender, String envelopeReceiver, Message message) {
        super();
        this.envelopeSender = envelopeSender;
        this.envelopeReceiver = envelopeReceiver;
        this.message = message;
    }
    
    public String getEnvelopeReceiver() {
        return envelopeReceiver;
    }
    
    public String getEnvelopeSender() {
        return envelopeSender;
    }
    
    public Message getMessage() {
        return message;
    }
    
    public static final WiserEmailMessage fromJsonObject(JsonObject jsonObject) {
        
        String envelopeSender = jsonObject.getString("sender");
        String envelopeReceiver = jsonObject.getString("receiver");
        
        byte[] content = Base64.decode(jsonObject.getString("message"));
        Message message = transformToMessage(content);
        
        return new WiserEmailMessage(envelopeSender, envelopeReceiver, message);
        
    }

    private static Message transformToMessage(byte[] content) {
        Message message;
        try {
            message = new MimeMessage(Session.getDefaultInstance(new Properties()), new ByteArrayInputStream(content));
        } catch (MessagingException e) {
            throw new IllegalArgumentException(e);
        }
        return message;
    }
    
}
