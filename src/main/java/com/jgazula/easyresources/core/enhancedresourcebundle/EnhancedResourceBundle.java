package com.jgazula.easyresources.core.enhancedresourcebundle;

import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorFactory;
import com.jgazula.easyresources.core.internal.properties.PropertiesReader;
import com.jgazula.easyresources.core.internal.util.FileUtil;

import java.io.IOException;
import java.util.Objects;

/**
 * An implementation of this interface is responsible for generating a Java file which
 * enhances the built-in {@link java.util.ResourceBundle} with typed methods for accessing
 * the values of keys.
 */
public interface EnhancedResourceBundle {

    /**
     * Generates the Java file with the enhanced capabilities.
     */
    void generate() throws IOException;

    /**
     * Creates an implementation of this interface.
     */
    static EnhancedResourceBundle create(ERBConfig config) {
        Objects.requireNonNull(config, "config cannot be null when creating a EnhancedResourceBundle instance.");
        return new ERBGenerator(config, new FileUtil(), new ClassGeneratorFactory(), new PropertiesReader());
    }
}
