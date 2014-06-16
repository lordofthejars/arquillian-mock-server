package org.jboss.arquillian.mockserver.fakeftp.client;

import java.util.Map;
import java.util.Properties;


public class FakeFtpConfiguration {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    public static final String FAKEFTP_PROPERTIES_FILENAME = "fakeftp.properties";

    private static final int DEFAULT_PORT = 12000;
    private static final String PORT_PROPERTY = "port";
    
    private static final String DEFAULT_CONFIGURATION_FILE = "fakeftp.yaml";
    private static final String CONFIGURATION_FILE_PROPERTY = "configurationFile";

    private int port;
    private String configurationFile;
    
    
    FakeFtpConfiguration(int port, String configurationFile) {
        this.port = port;
        this.configurationFile = configurationFile;
    }

    public int getPort() {
        return port;
    }
    
    public String getConfigurationFile() {
        return configurationFile;
    }

    public String toProperties() {
        return PORT_PROPERTY + " = " + getPort() + LINE_SEPARATOR + CONFIGURATION_FILE_PROPERTY + "=" + getConfigurationFile();
    }

    public static FakeFtpConfiguration fromProperties(Properties properties) {
        
        int port = DEFAULT_PORT;
        if (properties.containsKey(PORT_PROPERTY)) {
            port = Integer.parseInt((String) properties.get(PORT_PROPERTY));
        }
        
        String configurationFile = DEFAULT_CONFIGURATION_FILE;
        if(properties.containsKey(CONFIGURATION_FILE_PROPERTY)) {
            configurationFile = (String) properties.get(CONFIGURATION_FILE_PROPERTY);
        }

        return new FakeFtpConfiguration(port, configurationFile);
    }

    public static FakeFtpConfiguration fromMap(Map<String, String> map) {

        int port = DEFAULT_PORT;
        if (map.containsKey(PORT_PROPERTY)) {
            port = Integer.parseInt((String) map.get(PORT_PROPERTY));
        }
        
        String configurationFile = DEFAULT_CONFIGURATION_FILE;
        if(map.containsKey(CONFIGURATION_FILE_PROPERTY)) {
            configurationFile = (String) map.get(CONFIGURATION_FILE_PROPERTY);
        }

        return new FakeFtpConfiguration(port, configurationFile);

    }
    
}
