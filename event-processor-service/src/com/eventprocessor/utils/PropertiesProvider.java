package com.eventprocessor.utils;

import javax.annotation.Nonnull;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

/**
 * @author Artem
 * @since 12.06.2017.
 */
public class PropertiesProvider {
    private static final String CONFIG_FILE_PATH = "conf";

    private Properties properties;

    public void init() {
        properties = loadProperties();
    }

    @Nonnull
    public String getProperty(@Nonnull String propertyName) {
        return requireNonNull(properties.getProperty(propertyName), propertyName);
    }

    private static Properties loadProperties() {
        String path = System.getProperty(CONFIG_FILE_PATH);
        if (path == null) {
            throw new IllegalArgumentException("No config file path. Use -Dconf=... to configure path");
        }
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(path)) {
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Can not load properties", e);
        }
        return properties;
    }
}
