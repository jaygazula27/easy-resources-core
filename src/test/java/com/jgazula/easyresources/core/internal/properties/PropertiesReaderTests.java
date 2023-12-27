package com.jgazula.easyresources.core.internal.properties;

import com.jgazula.easyresources.core.testutil.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertiesReaderTests {

    private static final String PROPERTIES_FILE_NAME = "my_properties.properties";
    private static final String APP_BUNDLE_NAME = "AppBundle";
    private static final String APP_BUNDLE_PROPERTIES_FILE_NAME = "AppBundle.properties";
    private static final String APP_BUNDLE_EN_PROPERTIES_FILE_NAME = "AppBundle_en.properties";
    private static final String APP_BUNDLE_EN_US_PROPERTIES_FILE_NAME = "AppBundle_en_US.properties";
    private static final String APP_BUNDLE_FR_FR_PROPERTIES_FILE_NAME = "AppBundle_fr_FR.properties";
    private static final String APP_BUNDLE_JA_PROPERTIES_FILE_NAME = "AppBundle_ja.properties";

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
    void streamAllPropertiesFilesOfResourceBundle() throws IOException {
        // given
        var propertyFileNames = Set.of(APP_BUNDLE_PROPERTIES_FILE_NAME, APP_BUNDLE_EN_PROPERTIES_FILE_NAME,
                APP_BUNDLE_EN_US_PROPERTIES_FILE_NAME, APP_BUNDLE_FR_FR_PROPERTIES_FILE_NAME,
                APP_BUNDLE_JA_PROPERTIES_FILE_NAME);

        propertyFileNames.forEach(fileName -> {
            try {
                tmpDir.resolve(fileName).toFile().createNewFile();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });

        // create dummy files in same dir as well for negative test case
        for (int i = 0; i < 3; i++) {
            tmpDir.resolve(TestHelper.randomAlphabetic()).toFile().createNewFile();
        }

        // when and then
        try (Stream<Path> files = propertiesReader.getResourceBundlePropertyFiles(tmpDir, APP_BUNDLE_NAME)) {
            var collectedFileNames = files
                    .map(path -> path.toFile().getName())
                    .collect(Collectors.toList());

            assertThat(collectedFileNames).hasSameSizeAs(propertyFileNames);
            assertThat(collectedFileNames).hasSameElementsAs(propertyFileNames);
        }

    }
}
