package com.jgazula.typesaferesources.core.propertiesconstants;

import com.jgazula.typesaferesources.core.internal.classgeneration.ClassGeneratorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PCGenerator implements PropertiesConstants {

    private static final Logger LOGGER = LoggerFactory.getLogger(PCGenerator.class);

    private final PCConfig config;
    private final ClassGeneratorFactory generatorFactory;

    PCGenerator(PCConfig config, ClassGeneratorFactory generatorFactory) {
        this.config = config;
        this.generatorFactory = generatorFactory;
    }

    @Override
    public void generate() {
        for (PCFileConfig fileConfig : config.fileConfigs()) {

        }
    }

    private void generateForFileConfig() {

    }
}
