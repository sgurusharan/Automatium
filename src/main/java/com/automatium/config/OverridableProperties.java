package com.automatium.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Gurusharan on 19-11-2016.
 */
public class OverridableProperties {
    private Properties configuration;

    public OverridableProperties(String configFileName) throws IOException{
        configuration = new Properties();
        if (configFileName != null) {
            configuration.load(new FileInputStream(configFileName));
        }
    }

    public String get(String config, String defaultValue) {
        String configValue = System.getProperty(config, System.getenv(config));
        if (configValue == null) {
            configValue = configuration.getProperty(config, defaultValue);
        }

        return configValue;
    }

    public String get(String config) {
        return get(config, null);
    }
}
