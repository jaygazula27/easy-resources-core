package com.jgazula.easyresources.core.enhancedresourcebundle;

import com.jgazula.easyresources.core.internal.classgeneration.ClassGenerator;

/**
 * Generates a Java class specifically for creating an enhanced resource bundle.
 */
public interface ERBClassGenerator extends ClassGenerator {

    /**
     * Adds a constructor and class fields which are necessary for the enhanced resource bundle functionality.
     */
    void initialize();


}
