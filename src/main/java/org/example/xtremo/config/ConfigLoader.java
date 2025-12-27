package org.example.xtremo.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Abdelrahman
 */
public class ConfigLoader {

    private static final Properties properties = new Properties();
    private static final String PROPERTIES_PATH = "application.properties";
    
    
    private ConfigLoader() {}


    static {
        try (InputStream input = ConfigLoader.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_PATH)) {

            if (input == null) {
                throw new RuntimeException("Unable to find application.properties");
            }

            properties.load(input);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load configuration");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}