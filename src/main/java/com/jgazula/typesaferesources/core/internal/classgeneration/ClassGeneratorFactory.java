package com.jgazula.typesaferesources.core.internal.classgeneration;

/**
 * A factory for instances of {@link ClassGenerator}.
 * Useful for dependency injection when multiple instances need to be created.
 */
public class ClassGeneratorFactory {

    /**
     * Returns a {@link ClassGenerator} instance backed by the {@link PoetClassGenerator} implementation.
     */
    public ClassGenerator getGenerator(PoetClassGeneratorConfig config) {
        return new PoetClassGenerator(config);
    }
}
