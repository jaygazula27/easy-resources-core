package com.jgazula.easyresources.core.enhancedresourcebundle;

import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorConfig;
import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorFactory;
import com.jgazula.easyresources.core.internal.properties.PropertiesParser;
import com.jgazula.easyresources.core.internal.properties.PropertiesReader;
import com.jgazula.easyresources.core.internal.util.FileUtil;
import com.jgazula.easyresources.core.util.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

class ERBGenerator implements EnhancedResourceBundle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ERBGenerator.class);

    private final ERBConfig config;
    private final FileUtil fileUtil;
    private final ClassGeneratorFactory generatorFactory;
    private final PropertiesReader propertiesReader;
    private final MessageFormat messageFormat;
    private final PropertiesParser propertiesParser;

    ERBGenerator(ERBConfig config, FileUtil fileUtil, ClassGeneratorFactory generatorFactory,
                 PropertiesReader propertiesReader, MessageFormat messageFormat, PropertiesParser propertiesParser) {
        this.config = config;
        this.fileUtil = fileUtil;
        this.generatorFactory = generatorFactory;
        this.propertiesReader = propertiesReader;
        this.messageFormat = messageFormat;
        this.propertiesParser = propertiesParser;
    }

    @Override
    public void generate() throws IOException {
        if (config.bundleConfigs().isEmpty()) {
            LOGGER.warn("No resource bundles have been configured. Skipping constants file generation.");
            return;
        }

        for (ERBBundleConfig bundleConfig : config.bundleConfigs()) {
            generateEnhancedResourceBundle(bundleConfig);
        }
    }

    private void generateEnhancedResourceBundle(ERBBundleConfig bundleConfig) throws IOException {
        LOGGER.debug("Generating enhanced resource bundle for {}", bundleConfig.bundlePath());

        if (!fileUtil.exists(Paths.get(bundleConfig.bundlePath().toString(), bundleConfig.bundleName()))) {
            throw new ValidationException("Resource bundle %s not found in %s", bundleConfig.bundleName(),
                    bundleConfig.bundlePath().toString());
        }

        ResourceBundle bundle = propertiesReader.getBundle(bundleConfig.bundleName(), bundleConfig.bundlePath());
        LOGGER.debug("Successfully loaded {} resource bundle in {}", bundleConfig.bundleName(), bundleConfig.bundlePath());

        // Sort the keys for deterministic ordering
        // (which makes testing easier as well)
        var keySet = bundle.keySet();
        var keys = new ArrayList<>(keySet);
        Collections.sort(keys);

        if (keys.isEmpty()) {
            LOGGER.warn("The resource bundle {} is empty. Skipping enhancing of resource bundle.", bundleConfig.bundleName());
        } else {
            var poetConfig = ClassGeneratorConfig.builder()
                    .generatedBy(config.generatedBy())
                    .packageName(bundleConfig.generatedPackageName())
                    .className(bundleConfig.generatedClassName())
                    .build();
            ERBClassGenerator classGenerator = generatorFactory.getERBClassGenerator(poetConfig);
            classGenerator.initialize();

            for (var key : keys) {
                String value = bundle.getString(key);
                generateForKey(classGenerator, key, value);
            }

            Path writtenPath = classGenerator.write(config.destinationDir());
            LOGGER.debug("Wrote enhanced resource bundle to {}", writtenPath);
            LOGGER.info("Generated enhanced resource bundle for {}", bundleConfig.bundleName());
        }
    }

    private void generateForKey(ERBClassGenerator classGenerator, String key, String value) {
        messageFormat.applyPattern(value);
        var formats = messageFormat.getFormatsByArgumentIndex();

        var argTypes = Arrays.stream(formats)
                .map(this::mapFormatToArgType)
                .collect(Collectors.toList());
        classGenerator.addMethod(key, propertiesParser.keyToMethodName(key), argTypes);
    }

    private Type mapFormatToArgType(Format format) {
        if (format instanceof NumberFormat) {
            // handles number and choice format types
            // treat it as a long
            return Long.class;
        } else if (format instanceof DateFormat) {
            // handles date and time format types
            // treat it as a Date
            return Date.class;
        } else {
            // no format type is given
            // treat it as a String
            return String.class;
        }
    }
}
