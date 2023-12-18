package com.jgazula.easyresources.core.enhancedresourcebundle;

import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorConfig;
import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorFactory;
import com.jgazula.easyresources.core.internal.properties.PropertiesParser;
import com.jgazula.easyresources.core.internal.properties.PropertiesReader;
import com.jgazula.easyresources.core.internal.util.FileUtil;
import com.jgazula.easyresources.core.testutil.TestConstants;
import com.jgazula.easyresources.core.util.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListResourceBundle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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

    @Mock
    private FileUtil fileUtil;

    @Mock
    private ClassGeneratorFactory generatorFactory;

    @Mock
    private PropertiesReader propertiesReader;

    @Mock
    private PropertiesParser propertiesParser;

    private MessageFormat messageFormat;

    @BeforeEach
    void setUp() {
        messageFormat = new MessageFormat("");
    }

    @Test
    void nothingToGenerateWhenNoBundleConfigs() throws IOException {
        // given
        var config = ERBConfig.builder()
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .destinationDir(Paths.get(TestConstants.DESTINATION_DIR))
                .build();

        // when
        new ERBGenerator(config, fileUtil, generatorFactory, propertiesReader, messageFormat, propertiesParser)
                .generate();

        // then
        verify(generatorFactory, never()).getERBClassGenerator(any(ClassGeneratorConfig.class));
    }

    @Test
    void validateResourceBundleNotFound() {
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

        when(fileUtil.exists(any(Path.class))).thenReturn(false);

        // when
        var generator = new ERBGenerator(config, fileUtil, generatorFactory, propertiesReader,
                messageFormat, propertiesParser);

        // then
        assertThatExceptionOfType(ValidationException.class).isThrownBy(generator::generate);
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

        when(fileUtil.exists(any(Path.class))).thenReturn(true);

        when(propertiesReader.getBundle(TestConstants.TEST_RESOURCE_BUNDLE_NAME, TestConstants.TEST_RESOURCE_BUNDLE_PATH))
                .thenReturn(new EmptyResources());

        // when
        new ERBGenerator(config, fileUtil, generatorFactory, propertiesReader, messageFormat, propertiesParser)
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

        when(fileUtil.exists(any(Path.class))).thenReturn(true);

        var resourceBundle = new TestResources();
        when(propertiesReader.getBundle(TestConstants.TEST_RESOURCE_BUNDLE_NAME, TestConstants.TEST_RESOURCE_BUNDLE_PATH))
                .thenReturn(resourceBundle);

        var classGenerator = mock(ERBClassGenerator.class);
        when(classGenerator.initialize()).thenReturn(classGenerator);

        when(generatorFactory.getERBClassGenerator(any(ClassGeneratorConfig.class))).thenReturn(classGenerator);

        when(propertiesParser.keyToMethodName(anyString())).thenCallRealMethod();

        // when
        new ERBGenerator(config, fileUtil, generatorFactory, propertiesReader, messageFormat, propertiesParser)
                .generate();

        // then
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Type>> captor = ArgumentCaptor.forClass(List.class);
        verify(classGenerator, times(resourceBundle.getContents().length))
                .addMethod(anyString(), anyString(), captor.capture());

        var allArgTypesIter = captor.getAllValues().iterator();
        assertThat(allArgTypesIter.next()).isEmpty();
        assertThat(allArgTypesIter.next()).containsExactly(String.class);
        assertThat(allArgTypesIter.next()).containsExactly(String.class, String.class);
        assertThat(allArgTypesIter.next()).containsExactly(Long.class);
        assertThat(allArgTypesIter.next()).containsExactly(Date.class);
        assertThat(allArgTypesIter.hasNext()).isFalse();

        verify(classGenerator).write(eq(destinationDir));
    }

    private static class TestResources extends ListResourceBundle {
        @Override
        protected Object[][] getContents() {
            return new Object[][] {
                    {"key1", "No files in directory"},
                    {"key2", "Directory name is {0}"},
                    {"key3", "The directory {1} contains {0}"},
                    {"key4", "{0, number} files"},
                    {"key5", "Directory was created on {0, date}"}
            };
        }
    }

    private static class EmptyResources extends ListResourceBundle {
        @Override
        protected Object[][] getContents() {
            return new Object[0][];
        }
    }
}
