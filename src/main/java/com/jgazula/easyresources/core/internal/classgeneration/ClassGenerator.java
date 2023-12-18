package com.jgazula.easyresources.core.internal.classgeneration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Generates a Java file which can eventually be written to a given path.
 */
public interface ClassGenerator {

    /**
     * Adds a public static final field of type {@link String}.
     */
    ClassGenerator addPublicConstantString(String variableName, String variableValue);

    /**
     * Adds a private final field (presumably initialized in the constructor).
     */
    ClassGenerator addPrivateFinalField(ClassGeneratorVariable field);

    /**
     * Adds a public all-args constructor.
     */
    ClassGenerator addConstructorWithArgs(List<ClassGeneratorVariable> args);

    /**
     * Writes the generated Java file to the given directory.
     *
     * @return the path to the generated Java file
     */
    Path write(Path directory) throws IOException;
}
