package org.jboss.arquillian.mockserver.wiser.api;

import javax.json.JsonObject;

import org.subethamail.smtp.util.Base64;

public class WiserEmailMessage {

    private String envelopeSender;
    private String envelopeReceiver;
    
    private byte[] message;

    public WiserEmailMessage(String envelopeSender, String envelopeReceiver, byte[] message) {
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
    
    public byte[] getMessage() {
        return message;
    }
    
    public static final WiserEmailMessage fromJsonObject(JsonObject jsonObject) {
        
        String envelopeSender = jsonObject.getString("sender");
        String envelopeReceiver = jsonObject.getString("receiver");
        
        byte[] content = Base64.decode(jsonObject.getString("message"));
        
        return new WiserEmailMessage(envelopeSender, envelopeReceiver, content);
        
    }
    
}
