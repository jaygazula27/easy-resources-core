package com.jgazula.typesaferesources.core.internal.properties;

import static org.assertj.core.api.Assertions.assertThat;

import com.jgazula.typesaferesources.core.testutil.TestHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class PropertiesReaderTests {

  private static final String PROPERTIES_FILE_NAME = "my_properties.properties";

  @TempDir Path tmpDir;

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
    Map<String, String> result = new PropertiesReader().loadProperties(propertiesFile);

    // then
    assertThat(result)
        .isNotNull()
        .containsEntry(prop1Key, prop1Value)
        .containsEntry(prop2Key, prop2Value)
        .containsEntry(prop3Key, prop3Value);
  }
}
