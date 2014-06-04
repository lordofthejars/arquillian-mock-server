package org.jboss.arquillian.mockserver.wiser.client;

import java.util.Map;
import java.util.Properties;

public class WiserConfiguration {

    public static final String WISER_PROPERTIES_FILENAME = "wiser.properties";

    private static final int DEFAULT_PORT = 2500;
    private static final String PORT_PROPERTY = "port";

    private int port;

    WiserConfiguration(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String toProperties() {
        return PORT_PROPERTY + " = " + getPort();
    }

    public static WiserConfiguration fromProperties(Properties properties) {
        if (properties.containsKey(PORT_PROPERTY)) {
            return new WiserConfiguration(Integer.parseInt((String) properties.get(PORT_PROPERTY)));
        }

        return new WiserConfiguration(DEFAULT_PORT);
    }

    public static WiserConfiguration fromMap(Map<String, String> map) {

        if (map.containsKey(PORT_PROPERTY)) {
            return new WiserConfiguration(Integer.parseInt(map.get(PORT_PROPERTY)));
        }

        return new WiserConfiguration(DEFAULT_PORT);

    }

}
