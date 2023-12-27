package com.jgazula.easyresources.core.internal.properties;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
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
     * Loads the {@link ResourceBundle} with the given name and path.
     */
    public ResourceBundle getBundle(String bundleName, Path bundlePath) throws IOException {
        try (URLClassLoader loader = new URLClassLoader(new URL[]{bundlePath.toUri().toURL()})) {
            return ResourceBundle.getBundle(bundleName, Locale.getDefault(), loader);
        }
    }

    public Stream<Path> getResourceBundlePropertyFiles(Path bundlePath, String bundleName) throws IOException {
        var propertiesPattern = Pattern.compile(String.format(PROPERTIES_MATCHER_PATTERN_FORMAT, bundleName));

        return Files.find(bundlePath, 1,
                (path, attrs) -> attrs.isRegularFile() && propertiesPattern.matcher(path.toFile().getName()).matches(),
                FileVisitOption.FOLLOW_LINKS);
    }
}
