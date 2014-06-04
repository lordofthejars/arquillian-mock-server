package org.jboss.arquillian.mockserver.wiser.container.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.subethamail.smtp.util.Base64;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

@WebServlet(value = "/wiser")
public class SentMessageServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Wiser wiser = getWiser();
        
        List<WiserMessage> messages = wiser.getMessages();
        
        JsonArrayBuilder createArrayBuilder = Json.createArrayBuilder();
        
        for (WiserMessage wiserMessage : messages) {
            createArrayBuilder.add(
                                    Json.createObjectBuilder()
                                        .add("sender", wiserMessage.getEnvelopeSender())
                                        .add("receiver", wiserMessage.getEnvelopeReceiver())
                                        .add("message", Base64.encodeToString(wiserMessage.getData(), true))        
                                        );
        }
        
        JsonObjectBuilder model = Json.createObjectBuilder();
        model.add("messages", createArrayBuilder);
        
        JsonObject jsonObject = model.build();
        
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.write(jsonObject);
        
    }

    private Wiser getWiser() {
        Wiser wiser = (Wiser) getServletContext().getAttribute("wiser");
        return wiser;
    }

    
    
}
