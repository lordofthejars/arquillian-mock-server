package org.jboss.arquillian.mockserver.common.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

    public static Reader get(URL url) throws IOException {
        
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        
        return new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        
    }
    
}
