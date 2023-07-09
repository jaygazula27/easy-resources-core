package com.jgazula.easyresources.core.internal.properties;

import static org.assertj.core.api.Assertions.assertThat;

import com.jgazula.easyresources.core.testutil.TestHelper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class PropertiesReaderTests {

    private static final String PROPERTIES_FILE_NAME = "my_properties.properties";
    private static final String RESOURCE_BUNDLE_NAME = "MyResources";
    private static final String PROPERTIES_EXTENSION = "properties";

    @TempDir
    Path tmpDir;

    private PropertiesReader propertiesReader;

    @BeforeEach
    void setUp() {
        propertiesReader = new PropertiesReader();
    }

    @Test
    public void propertiesAreLoadedProperly() throws IOException {
        // given
        String prop1Key = TestHelper.randomAlphabetic();
        String prop1Value = TestHelper.randomAlphabetic();
        String prop2Key = TestHelper.randomAlphabetic();
        String prop2Value = TestHelper.randomAlphabetic();
        String prop3Key = TestHelper.randomAlphabetic();
        String prop3Value = TestHelper.randomAlphabetic();

        Properties properties = new Properties();
        properties.setProperty(prop1Key, prop1Value);
        properties.setProperty(prop2Key, prop2Value);
        properties.setProperty(prop3Key, prop3Value);

        Path propertiesFile = tmpDir.resolve(PROPERTIES_FILE_NAME);
        try (BufferedWriter writer = Files.newBufferedWriter(propertiesFile, StandardCharsets.UTF_8)) {
            properties.store(writer, null);
        }

        // when
        Map<String, String> result = propertiesReader.loadProperties(propertiesFile);

        // then
        assertThat(result)
                .isNotNull()
                .containsEntry(prop1Key, prop1Value)
                .containsEntry(prop2Key, prop2Value)
                .containsEntry(prop3Key, prop3Value);
    }

    @Test
    public void resourceBundleInitialized() throws IOException {
        // given
        String prop1Key = TestHelper.randomAlphabetic();
        String prop1Value = TestHelper.randomAlphabetic();
        String prop2Key = TestHelper.randomAlphabetic();
        String prop2Value = TestHelper.randomAlphabetic();
        String prop3Key = TestHelper.randomAlphabetic();
        String prop3Value = TestHelper.randomAlphabetic();

        Properties properties = new Properties();
        properties.setProperty(prop1Key, prop1Value);
        properties.setProperty(prop2Key, prop2Value);
        properties.setProperty(prop3Key, prop3Value);

        Path propertiesFile = tmpDir.resolve(String.format("%s.%s", RESOURCE_BUNDLE_NAME, PROPERTIES_EXTENSION));
        try (BufferedWriter writer = Files.newBufferedWriter(propertiesFile, StandardCharsets.UTF_8)) {
            properties.store(writer, null);
        }

        // when
        ResourceBundle bundle = propertiesReader.getBundle(RESOURCE_BUNDLE_NAME, tmpDir);

        // then
        assertThat(bundle).isNotNull();
        assertThat(bundle.getString(prop1Key)).isEqualTo(prop1Value);
        assertThat(bundle.getString(prop2Key)).isEqualTo(prop2Value);
        assertThat(bundle.getString(prop3Key)).isEqualTo(prop3Value);
    }
}
