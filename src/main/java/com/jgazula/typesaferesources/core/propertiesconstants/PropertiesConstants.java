package com.jgazula.typesaferesources.core.propertiesconstants;

import com.jgazula.typesaferesources.core.internal.classgeneration.ClassGeneratorFactory;

import java.util.Objects;

public interface PropertiesConstants {

    void generate();

    static PropertiesConstants create(PCConfig config) {
        Objects.requireNonNull(config);
        return new PCGenerator(config, new ClassGeneratorFactory());
    }
}
