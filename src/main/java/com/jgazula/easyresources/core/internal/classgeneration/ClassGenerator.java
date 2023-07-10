package com.jgazula.easyresources.core.internal.classgeneration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Map;

/**
 * Generates a Java file which can eventually be written to a given path.
 */
public interface ClassGenerator {

    /**
     * Adds a public static final field of type {@link String}.
     */
    void addPublicConstantString(String variableName, String variableValue);

    /**
     * Adds a private final field (presumably initialized in the constructor).
     */
    void addPrivateFinalField(Type type, String variableName);

    /**
     * Adds a public all-args constructor.
     * @param args key-value pairs of variable type and name
     */
    void addConstructorWithArgs(Map<Type, String> args);

    /**
     * Writes the generated Java file to the given directory.
     *
     * @return the path to the generated Java file
     */
    Path write(Path directory) throws IOException;
}
