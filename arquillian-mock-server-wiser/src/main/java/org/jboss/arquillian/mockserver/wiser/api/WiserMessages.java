package org.jboss.arquillian.mockserver.wiser.api;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.jboss.arquillian.mockserver.common.client.HttpClient;

public class WiserMessages {

    private URL wiserURl;
    
    public WiserMessages(URL wiserUrl) {
        this.wiserURl = wiserUrl;
    }
    
    public static final List<WiserEmailMessage> fromJsonObject(JsonObject jsonObject) {
        
        List<WiserEmailMessage> wiserEmailMessages = new ArrayList<>();
        
        JsonArray arrayOfMessages = jsonObject.getJsonArray("messages");
        
        for (int i = 0; i< arrayOfMessages.size(); i++) {
            wiserEmailMessages.add(WiserEmailMessage.fromJsonObject(arrayOfMessages.getJsonObject(i)));
        }
        
        return wiserEmailMessages;
    }
    
    private List<WiserEmailMessage> getJsonMessages() throws MalformedURLException, IOException {
        
        Reader messagesReader = HttpClient.get(wiserURl);
        JsonReader jsonReader = Json.createReader(messagesReader);
        
        JsonObject messages = jsonReader.readObject();
        
        List<WiserEmailMessage> allMessages = WiserMessages.fromJsonObject(messages);
        
        messagesReader.close();
        
        return allMessages;
        
    }
    
    public List<WiserEmailMessage> messages() {
        try {
            return getJsonMessages();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
}
