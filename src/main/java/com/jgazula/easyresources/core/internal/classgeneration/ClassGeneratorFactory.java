package com.jgazula.easyresources.core.internal.classgeneration;

import com.jgazula.easyresources.core.enhancedresourcebundle.ERBClassGenerator;
import com.jgazula.easyresources.core.enhancedresourcebundle.PoetERBClassGenerator;

/**
 * A factory for instances of {@link ClassGenerator}. Useful for dependency injection when multiple
 * instances need to be created.
 */
public class ClassGeneratorFactory {

    /**
     * Returns a {@link ClassGenerator} instance backed by the {@link PoetClassGenerator}
     * implementation.
     */
    public ClassGenerator getGenerator(ClassGeneratorConfig config) {
        return new PoetClassGenerator(config);
    }

    /**
     * Returns a {@link ERBClassGenerator} that can create enhanced resource bundle Java files.
     */
    public ERBClassGenerator getERBClassGenerator(ClassGeneratorConfig config) {
        return new PoetERBClassGenerator(config);
    }
}
