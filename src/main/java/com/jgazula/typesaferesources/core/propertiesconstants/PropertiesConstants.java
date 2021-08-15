package com.jgazula.typesaferesources.core.propertiesconstants;

import com.jgazula.typesaferesources.core.internal.classgeneration.ClassGeneratorFactory;
import com.jgazula.typesaferesources.core.internal.properties.PropertiesParser;
import com.jgazula.typesaferesources.core.internal.properties.PropertiesReader;

import java.io.IOException;
import java.util.Objects;

public interface PropertiesConstants {

    void generate() throws IOException;

    static PropertiesConstants create(PCConfig config) {
        Objects.requireNonNull(config);
        return new PCGenerator(config, new ClassGeneratorFactory(), new PropertiesReader(), new PropertiesParser());
    }
}
