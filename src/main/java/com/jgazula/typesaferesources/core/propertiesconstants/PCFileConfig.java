package com.jgazula.typesaferesources.core.propertiesconstants;

import com.jgazula.typesaferesources.core.internal.util.ImmutableStyle;
import org.immutables.value.Value;

import java.nio.file.Path;

@ImmutableStyle
@Value.Immutable
public interface PCFileConfig {

    Path propertiesPath();
    String generatedClassName();
    String generatedPackageName();

    @Value.Check
    default void check() {
        if (generatedClassName().isEmpty()) {
            throw new IllegalArgumentException("'generatedClassName' configuration cannot be empty.");
        } else if (generatedPackageName().isEmpty()) {
            throw new IllegalArgumentException("'generatedPackageName' configuration cannot be empty.");
        }
    }
}
