package com.jgazula.easyresources.core.enhancedresourcebundle;

import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorConfig;
import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorFactory;
import com.jgazula.easyresources.core.internal.properties.PropertiesParser;
import com.jgazula.easyresources.core.internal.properties.PropertiesReader;
import com.jgazula.easyresources.core.testutil.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ERBGeneratorTests {

    private static final String PROPERTIES_FILE_NAME = "my_properties.properties";

    @Mock
    private ClassGeneratorFactory generatorFactory;

    @Mock
    private PropertiesReader propertiesReader;

    @Mock
    private PropertiesParser propertiesParser;

    @TempDir
    Path tmpDir;

    @Test
    void nothingToGenerateWhenNoBundleConfigs() throws IOException {
        // given
        var config = ERBConfig.builder()
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .destinationDir(Paths.get(TestConstants.DESTINATION_DIR))
                .build();

        // when
        new ERBGenerator(config, generatorFactory, propertiesReader, new MessageFormat(""), propertiesParser)
                .generate();

        // then
        verify(generatorFactory, never()).getERBClassGenerator(any(ClassGeneratorConfig.class));
    }

    @Test
    void nothingToGenerateWhenBundleIsEmpty() throws IOException {
        // given
        var bundleConfig = ERBBundleConfig.builder()
                .bundleName(TestConstants.TEST_RESOURCE_BUNDLE_NAME)
                .bundlePath(TestConstants.TEST_RESOURCE_BUNDLE_PATH)
                .generatedClassName(TestConstants.TEST_CLASS_NAME)
                .generatedPackageName(TestConstants.TEST_PACKAGE_NAME)
                .build();

        var config = ERBConfig.builder()
                .bundleConfigs(Collections.singletonList(bundleConfig))
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .destinationDir(Paths.get(TestConstants.DESTINATION_DIR))
                .build();

        when(propertiesReader.getResourceBundlePropertyFiles(TestConstants.TEST_RESOURCE_BUNDLE_PATH, TestConstants.TEST_RESOURCE_BUNDLE_NAME))
                .thenReturn(Stream.empty());

        // when
        new ERBGenerator(config, generatorFactory, propertiesReader, new MessageFormat(""), propertiesParser)
                .generate();

        // then
        verify(generatorFactory, never()).getERBClassGenerator(any(ClassGeneratorConfig.class));
    }

    @Test
    void successfullyGeneratedEnhancedBundle() throws IOException {
        // given
        var bundleConfig = ERBBundleConfig.builder()
                .bundleName(TestConstants.TEST_RESOURCE_BUNDLE_NAME)
                .bundlePath(TestConstants.TEST_RESOURCE_BUNDLE_PATH)
                .generatedClassName(TestConstants.TEST_CLASS_NAME)
                .generatedPackageName(TestConstants.TEST_PACKAGE_NAME)
                .build();

        var destinationDir = Paths.get(TestConstants.DESTINATION_DIR);
        var config = ERBConfig.builder()
                .bundleConfigs(Collections.singletonList(bundleConfig))
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .destinationDir(destinationDir)
                .build();

        Properties properties = new Properties();
        properties.setProperty("key1", "No files in directory");
        properties.setProperty("key2", "The directory {1} contains {0}");
        properties.setProperty("key3", "{0,number} files");
        properties.setProperty("key4", "{0,number,integer} planets");
        properties.setProperty("key5", "This cost over {0,number,currency}");
        properties.setProperty("key6", "I got a {0,number,percent} on the quiz");
        properties.setProperty("key7", "Directory was created on {0,date}");
        properties.setProperty("key8", "The current time is {0,time}");
        properties.setProperty("key9", "There {0,choice,0#are no files|1#is one file|1<are {0,number,integer} files}");

        Path propertiesFile = tmpDir.resolve(PROPERTIES_FILE_NAME);
        try (BufferedWriter writer = Files.newBufferedWriter(propertiesFile, StandardCharsets.UTF_8)) {
            properties.store(writer, null);
        }

        when(propertiesReader.getResourceBundlePropertyFiles(TestConstants.TEST_RESOURCE_BUNDLE_PATH, TestConstants.TEST_RESOURCE_BUNDLE_NAME))
                .thenReturn(Stream.of(propertiesFile));
        when(propertiesReader.loadProperties(propertiesFile)).thenCallRealMethod();

        var classGenerator = mock(ERBClassGenerator.class);
        when(classGenerator.initialize()).thenReturn(classGenerator);

        when(generatorFactory.getERBClassGenerator(any(ClassGeneratorConfig.class))).thenReturn(classGenerator);

        when(propertiesParser.keyToMethodName(anyString())).thenCallRealMethod();

        // when
        new ERBGenerator(config, generatorFactory, propertiesReader, new MessageFormat(""), propertiesParser)
                .generate();

        // then
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Type>> captor = ArgumentCaptor.forClass(List.class);
        verify(classGenerator, times(properties.size()))
                .addMethod(anyString(), anyString(), captor.capture());

        var allArgTypesIter = captor.getAllValues().iterator();
        assertThat(allArgTypesIter.next()).isEmpty();
        assertThat(allArgTypesIter.next()).containsExactly(String.class, String.class);
        assertThat(allArgTypesIter.next()).containsExactly(int.class);
        assertThat(allArgTypesIter.next()).containsExactly(int.class);
        assertThat(allArgTypesIter.next()).containsExactly(BigDecimal.class);
        assertThat(allArgTypesIter.next()).containsExactly(BigDecimal.class);
        assertThat(allArgTypesIter.next()).containsExactly(Date.class);
        assertThat(allArgTypesIter.next()).containsExactly(Date.class);
        assertThat(allArgTypesIter.next()).containsExactly(int.class);
        assertThat(allArgTypesIter.hasNext()).isFalse();

        verify(classGenerator).write(eq(destinationDir));
    }
}
