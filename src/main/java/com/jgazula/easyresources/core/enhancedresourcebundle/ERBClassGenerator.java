package com.jgazula.easyresources.core.enhancedresourcebundle;

import com.jgazula.easyresources.core.internal.classgeneration.ClassGenerator;

/**
 * Generates a Java class specifically for creating an enhanced resource bundle.
 */
public interface ERBClassGenerator extends ClassGenerator {

    /**
     * Adds a constructor suitable for the enhanced resource bundle functionality.
     * This assumes that the necessary class member fields have already been added.
     */
    void addERBConstructor();
}
