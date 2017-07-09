package com.eventprocessor.utils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Artem
 * @since 12.06.2017.
 */
public class TestablePropertiesProvider extends PropertiesProvider {
    private final Map<String, String> properties = new HashMap<>();

    @Nonnull
    @Override
    public String getProperty(@Nonnull String propertyName) {
        return properties.get(propertyName);
    }

    public void putProperties(String property, String value) {
        properties.put(property, value);
    }
}
