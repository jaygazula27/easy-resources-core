package com.jgazula.easyresources.core.propertiesconstants;

import com.jgazula.easyresources.core.internal.classgeneration.ClassGenerator;
import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorConfig;
import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorFactory;
import com.jgazula.easyresources.core.internal.properties.PropertiesParser;
import com.jgazula.easyresources.core.internal.properties.PropertiesReader;
import com.jgazula.easyresources.core.internal.util.FileUtil;
import com.jgazula.easyresources.core.util.ValidationException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of {@link PropertiesConstants} which will generate a Java file of constants by
 * parsing through the given properties files.
 */
class PCGenerator implements PropertiesConstants {

    private static final Logger LOGGER = LoggerFactory.getLogger(PCGenerator.class);

    private final PropertiesConstantsConfig config;
    private final ClassGeneratorFactory generatorFactory;
    private final PropertiesReader propertiesReader;
    private final PropertiesParser propertiesParser;
    private final FileUtil fileUtil;

    PCGenerator(PropertiesConstantsConfig config, ClassGeneratorFactory generatorFactory,
                PropertiesReader propertiesReader, PropertiesParser propertiesParser, FileUtil fileUtil) {
        this.config = config;
        this.generatorFactory = generatorFactory;
        this.propertiesReader = propertiesReader;
        this.propertiesParser = propertiesParser;
        this.fileUtil = fileUtil;
    }

    @Override
    public void generate() throws IOException {
        if (config.fileConfigs().isEmpty()) {
            LOGGER.warn("No properties files have been configured. Skipping constants file generation.");
            return;
        }

        for (PropertiesConstantsFileConfig fileConfig : config.fileConfigs()) {
            generateFile(fileConfig);
        }
    }

    private void generateFile(PropertiesConstantsFileConfig fileConfig) throws IOException {
        LOGGER.debug("Generating constants file for {}", fileConfig.propertiesPath());

        if (!fileUtil.exists(fileConfig.propertiesPath())) {
            throw new ValidationException("File %s does not exist", fileConfig.propertiesPath().toString());
        }

        var poetConfig = ClassGeneratorConfig.builder()
                .generatedBy(config.generatedBy())
                .packageName(fileConfig.generatedPackageName())
                .className(fileConfig.generatedClassName())
                .build();

        ClassGenerator generator = generatorFactory.getGenerator(poetConfig);

        Map<String, String> properties = propertiesReader.loadProperties(fileConfig.propertiesPath());
        LOGGER.debug("Successfully loaded {} properties from {}", properties.size(), fileConfig.propertiesPath());

        if (properties.isEmpty()) {
            LOGGER.warn("The properties file {} is empty. Skipping constants file generation.",
                    fileConfig.propertiesPath());
        } else {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                try {
                    String variableName = propertiesParser.keyToStaticFinalVariable(entry.getKey());
                    generator.addPublicConstantString(variableName, entry.getKey());
                } catch (IllegalArgumentException e) {
                    LOGGER.debug("Unable to parse or generate variable for key {} in file {}", entry.getKey(),
                            fileConfig.propertiesPath().toFile(), e);

                    throw new ValidationException("Invalid property key %s in file %s", entry.getKey(),
                            fileConfig.propertiesPath().toString());
                }
            }

            Path writtenPath = generator.write(config.destinationDir());
            LOGGER.debug("Wrote properties to {}", writtenPath);
            LOGGER.info("Generated constants file for {}", fileConfig.propertiesPath());
        }
    }
}
