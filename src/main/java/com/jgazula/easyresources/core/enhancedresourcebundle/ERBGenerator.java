package com.jgazula.easyresources.core.enhancedresourcebundle;

import com.jgazula.easyresources.core.internal.classgeneration.ClassGenerator;
import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorConfig;
import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorFactory;
import com.jgazula.easyresources.core.internal.properties.PropertiesReader;
import com.jgazula.easyresources.core.internal.util.FileUtil;
import com.jgazula.easyresources.core.util.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.Set;

class ERBGenerator implements EnhancedResourceBundle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ERBGenerator.class);

    private final ERBConfig config;
    private final FileUtil fileUtil;
    private final ClassGeneratorFactory generatorFactory;
    private final PropertiesReader propertiesReader;

    ERBGenerator(ERBConfig config, FileUtil fileUtil, ClassGeneratorFactory generatorFactory,
                 PropertiesReader propertiesReader) {
        this.config = config;
        this.fileUtil = fileUtil;
        this.generatorFactory = generatorFactory;
        this.propertiesReader = propertiesReader;
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

        Set<String> keys = bundle.keySet();
        if (keys.isEmpty()) {
            LOGGER.warn("The resource bundle {} is empty. Skipping enhancing of resource bundle.", bundleConfig.bundleName());
        } else {
            var poetConfig = ClassGeneratorConfig.builder()
                    .generatedBy(config.generatedBy())
                    .packageName(bundleConfig.generatedPackageName())
                    .className(bundleConfig.generatedClassName())
                    .build();
            ClassGenerator generator = generatorFactory.getGenerator(poetConfig);


        }
    }
}
