package com.jgazula.typesaferesources.core.propertiesconstants;

import com.jgazula.typesaferesources.core.internal.classgeneration.ClassGenerator;
import com.jgazula.typesaferesources.core.internal.classgeneration.ClassGeneratorFactory;
import com.jgazula.typesaferesources.core.internal.classgeneration.PoetClassGeneratorConfig;
import com.jgazula.typesaferesources.core.internal.properties.PropertiesParser;
import com.jgazula.typesaferesources.core.internal.properties.PropertiesReader;
import com.jgazula.typesaferesources.core.testutil.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PCGeneratorTests {

    @Mock
    private ClassGeneratorFactory generatorFactory;

    @Mock
    private PropertiesReader propertiesReader;

    @Mock
    private PropertiesParser propertiesParser;

    @Test
    public void nothingToGenerateWhenNoFileConfigs() throws IOException {
        // given
        Path destinationDir = Paths.get(TestConstants.DESTINATION_DIR);
        PCConfig config = PCConfig.builder()
                .destinationDir(destinationDir)
                .build();

        // when
        new PCGenerator(config, generatorFactory, propertiesReader, propertiesParser).generate();

        // then
        verify(generatorFactory, never()).getGenerator(any(PoetClassGeneratorConfig.class));
    }

    @Test
    public void nothingToGenerateWhenPropertiesFileIsEmpty() throws IOException {
        // given
        Path propertiesPath = Paths.get(TestConstants.TEST_PROPERTIES_FILE);
        Path destinationDir = Paths.get(TestConstants.DESTINATION_DIR);

        PCFileConfig fileConfig = PCFileConfig.builder()
                .generatedPackageName(TestConstants.TEST_PACKAGE_NAME)
                .generatedClassName(TestConstants.TEST_CLASS_NAME)
                .propertiesPath(propertiesPath)
                .build();

        PCConfig config = PCConfig.builder()
                .fileConfigs(Collections.singletonList(fileConfig))
                .destinationDir(destinationDir)
                .build();


        ClassGenerator classGenerator = mock(ClassGenerator.class);
        when(generatorFactory.getGenerator(any(PoetClassGeneratorConfig.class))).thenReturn(classGenerator);

        when(propertiesReader.loadProperties(propertiesPath)).thenReturn(new HashMap<>());

        // when
        new PCGenerator(config, generatorFactory, propertiesReader, propertiesParser).generate();

        // then
        verify(classGenerator, never()).addPublicConstantString(anyString(), anyString());
        verify(classGenerator).write(destinationDir);
    }
}
