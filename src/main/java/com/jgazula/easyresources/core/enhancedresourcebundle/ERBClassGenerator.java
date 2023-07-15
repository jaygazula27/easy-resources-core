package com.jgazula.easyresources.core.enhancedresourcebundle;

import com.jgazula.easyresources.core.internal.classgeneration.ClassGenerator;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Generates a Java class specifically for creating an enhanced resource bundle.
 */
public interface ERBClassGenerator extends ClassGenerator {

    /**
     * Adds a constructor and class fields which are necessary for the enhanced resource bundle functionality.
     */
    void initialize();

    /**
     * Adds a method with the given argument types. This should also create the necessary statements within
     * the method to implement the enhanced resource bundle functionality.
     */
    void addMethod(String name, List<Type> argTypes, String key);
}
