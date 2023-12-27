package com.jgazula.easyresources.core.enhancedresourcebundle;

import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorConfig;
import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorFactory;
import com.jgazula.easyresources.core.internal.properties.PropertiesParser;
import com.jgazula.easyresources.core.internal.properties.PropertiesReader;
import com.jgazula.easyresources.core.internal.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ERBGenerator implements EnhancedResourceBundle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ERBGenerator.class);

    private final ERBConfig config;
    private final ClassGeneratorFactory generatorFactory;
    private final PropertiesReader propertiesReader;
    private final MessageFormat messageFormat;
    private final PropertiesParser propertiesParser;

    ERBGenerator(ERBConfig config, ClassGeneratorFactory generatorFactory,
                 PropertiesReader propertiesReader, MessageFormat messageFormat, PropertiesParser propertiesParser) {
        this.config = config;
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

        // Ensure the keys are sorted for deterministic ordering
        // (which makes testing easier as well)
        var properties = new TreeMap<String, String>();
        try (Stream<Path> files =
                     propertiesReader.getResourceBundlePropertyFiles(bundleConfig.bundlePath(), bundleConfig.bundleName())) {
            files.forEach(path -> {
                try {
                    properties.putAll(propertiesReader.loadProperties(path));
                } catch (IOException e) {
                    throw new UncheckedIOException("Unable to load properties for file: " + path.toString(), e);
                }
            });
        }

        if (properties.isEmpty()) {
            LOGGER.warn("The resource bundle {} at path {} is empty. Skipping enhancing of resource bundle.",
                    bundleConfig.bundleName(), bundleConfig.bundlePath());
        } else {
            var poetConfig = ClassGeneratorConfig.builder()
                    .generatedBy(config.generatedBy())
                    .packageName(bundleConfig.generatedPackageName())
                    .className(bundleConfig.generatedClassName())
                    .build();
            ERBClassGenerator classGenerator = generatorFactory.getERBClassGenerator(poetConfig);
            classGenerator.initialize();

            for (var entry : properties.entrySet()) {
                generateForKey(classGenerator, entry.getKey(), entry.getValue());
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
            if (format instanceof DecimalFormat) {
                var decimalFormat = (DecimalFormat) format;
                if (!StringUtil.isNullOrEmpty(decimalFormat.getPositivePrefix()) ||
                        !StringUtil.isNullOrEmpty(decimalFormat.getPositiveSuffix())) {
                    // assume either percent or currency
                    return BigDecimal.class;
                }
            }

            // treat all other number formats as int
            return int.class;
        } else if (format instanceof DateFormat) {
            // treat it as a Date
            return Date.class;
        } else {
            // treat it as a String
            return String.class;
        }
    }
}
