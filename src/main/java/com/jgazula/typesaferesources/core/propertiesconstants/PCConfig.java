package com.jgazula.typesaferesources.core.propertiesconstants;

import com.jgazula.typesaferesources.core.internal.util.ImmutableStyle;
import org.immutables.value.Value;

import java.nio.file.Path;
import java.util.List;

@ImmutableStyle
@Value.Immutable
public interface PCConfig {

    Path generatedSourcesDir();
    int maximumFields();
    List<PCFileConfig> fileConfigs();
}
