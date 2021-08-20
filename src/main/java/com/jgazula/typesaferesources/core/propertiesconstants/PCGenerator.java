package com.jgazula.typesaferesources.core.propertiesconstants;

import com.jgazula.typesaferesources.core.internal.classgeneration.ClassGenerator;
import com.jgazula.typesaferesources.core.internal.classgeneration.ClassGeneratorFactory;
import com.jgazula.typesaferesources.core.internal.classgeneration.ImmutablePoetClassGeneratorConfig;
import com.jgazula.typesaferesources.core.internal.classgeneration.PoetClassGeneratorConfig;
import com.jgazula.typesaferesources.core.internal.properties.PropertiesParser;
import com.jgazula.typesaferesources.core.internal.properties.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * An implementation of {@link PropertiesConstants} which will generate a Java file of constants by parsing
 * through the given properties files.
 */
class PCGenerator implements PropertiesConstants {

    private static final Logger LOGGER = LoggerFactory.getLogger(PCGenerator.class);

    private final PCConfig config;
    private final ClassGeneratorFactory generatorFactory;
    private final PropertiesReader propertiesReader;
    private final PropertiesParser propertiesParser;

    PCGenerator(PCConfig config, ClassGeneratorFactory generatorFactory, PropertiesReader propertiesReader,
                PropertiesParser propertiesParser) {
        this.config = config;
        this.generatorFactory = generatorFactory;
        this.propertiesReader = propertiesReader;
        this.propertiesParser = propertiesParser;
    }

    @Override
    public void generate() throws IOException {
        for (PCFileConfig fileConfig : config.fileConfigs()) {
            generateFile(fileConfig);
        }
    }

    private void generateFile(PCFileConfig fileConfig) throws IOException {
        LOGGER.info("Generating constants for {}", fileConfig.propertiesPath());

        PoetClassGeneratorConfig poetConfig = ImmutablePoetClassGeneratorConfig.builder()
                .packageName(fileConfig.generatedPackageName())
                .className(fileConfig.generatedClassName())
                .build();

        ClassGenerator generator = generatorFactory.getGenerator(poetConfig);

        Map<String, String> properties = propertiesReader.loadProperties(fileConfig.propertiesPath());
        LOGGER.debug("Successfully loaded {} properties from {}", properties.size(), fileConfig.propertiesPath());

        if (properties.isEmpty()) {
            LOGGER.warn("The properties file {} is empty. Skipping Java file generation.", fileConfig.propertiesPath());
        } else {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                String variableName = propertiesParser.keyToStaticFinalVariable(entry.getKey());
                generator.addPublicConstantString(variableName, entry.getValue());
            }

            Path writtenPath = generator.write(config.destinationDir());
            LOGGER.debug("Wrote properties to {}", writtenPath);
            LOGGER.info("Finished generating constants for {}", fileConfig.propertiesPath());
        }
    }
}
