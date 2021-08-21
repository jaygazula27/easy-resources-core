package com.jgazula.typesaferesources.core.propertiesconstants;

import com.jgazula.typesaferesources.core.internal.util.ImmutableStyle;
import java.nio.file.Path;
import org.immutables.value.Value;

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

  static Builder builder() {
    return ImmutablePCFileConfig.builder();
  }

  interface Builder {

    Builder propertiesPath(Path propertiesPath);

    Builder generatedClassName(String generatedClassName);

    Builder generatedPackageName(String generatedPackageName);

    PCFileConfig build();
  }
}
