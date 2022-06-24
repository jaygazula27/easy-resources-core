package com.jgazula.typesaferesources.core.internal.properties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

/**
 * A helper class used for reading a properties file.
 */
public class PropertiesReader {

    /**
     * Reads and loads the given properties file.
     */
    public Map<String, String> loadProperties(Path path) throws IOException {
        try (var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            var properties = new Properties();
            properties.load(reader);
            return (Map) properties;
        }
    }
}
