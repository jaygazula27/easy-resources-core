package com.jgazula.typesaferesources.core.propertiesconstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PCGenerator implements PropertiesConstants {

    private static final Logger LOGGER = LoggerFactory.getLogger(PCGenerator.class);

    private final PCConfig config;

    PCGenerator(PCConfig config) {
        this.config = config;
    }

    @Override
    public void generate() {
        for (PCFileConfig fileConfig : config.fileConfigs()) {

        }
    }

    private void generateForFileConfig() {

    }
}
