package com.jgazula.typesaferesources.core.propertiesconstants;

import java.util.Objects;

public interface PropertiesConstants {

    void generate();

    static PropertiesConstants create(PCConfig config) {
        Objects.requireNonNull(config);
        return new PCGenerator(config);
    }
}
