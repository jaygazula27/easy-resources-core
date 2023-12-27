package com.jgazula.easyresources.core.internal.properties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A helper class used for reading a properties file.
 */
public class PropertiesReader {

    private static final String PROPERTIES_MATCHER_PATTERN_FORMAT = "%s.*\\.properties";

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

    /**
     * Loads/streams all the properties files which begin with the given {@code bundleName} at the given {@code bundlePath}.
     * This enables reading all the possible properties for the given resource bundle.
     */
    public Stream<Path> getResourceBundlePropertyFiles(Path bundlePath, String bundleName) throws IOException {
        var propertiesPattern = Pattern.compile(String.format(PROPERTIES_MATCHER_PATTERN_FORMAT, bundleName));

        return Files.find(bundlePath, 1,
                (path, attrs) -> attrs.isRegularFile() && propertiesPattern.matcher(path.toFile().getName()).matches(),
                FileVisitOption.FOLLOW_LINKS);
    }
}
