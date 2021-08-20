package com.jgazula.typesaferesources.core.internal.classgeneration;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Generates a Java file which can eventually be written to a given path.
 */
public interface ClassGenerator {

    /**
     * Adds a public static final field of type {@link String}.
     */
    void addPublicConstantString(String variableName, String variableValue);

    /**
     * Writes the generated Java file to the given directory.
     * @return the path to the generated Java file
     */
    Path write(Path directory) throws IOException;
}
