package com.jgazula.easyresources.core.propertiesconstants;

import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorFactory;
import com.jgazula.easyresources.core.internal.properties.PropertiesParser;
import com.jgazula.easyresources.core.internal.properties.PropertiesReader;
import com.jgazula.easyresources.core.internal.util.FileUtil;

import java.io.IOException;
import java.util.Objects;

/**
 * An implementation of this interface is responsible for generating a Java file consisting of
 * constants which will be parsed from properties files.
 */
public interface PropertiesConstants {

    /**
     * Generates the Java file with the appropriate constants.
     */
    void generate() throws IOException;

    /**
     * Creates an implementation of this interface.
     */
    static PropertiesConstants create(PropertiesConstantsConfig config) {
        Objects.requireNonNull(config, "config cannot be null when creating a PropertiesConstants instance.");
        return new PCGenerator(config, new ClassGeneratorFactory(), new PropertiesReader(), new PropertiesParser(),
                new FileUtil());
    }
}
