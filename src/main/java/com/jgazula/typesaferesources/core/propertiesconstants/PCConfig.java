package com.jgazula.typesaferesources.core.propertiesconstants;

import com.jgazula.typesaferesources.core.internal.util.ImmutableStyle;
import org.immutables.value.Value;

import java.nio.file.Path;
import java.util.List;

@ImmutableStyle
@Value.Immutable
public interface PCConfig {

    List<PCFileConfig> fileConfigs();
    Path destinationDir();

    static Builder builder() {
        return ImmutablePCConfig.builder();
    }

    interface Builder {
        Builder fileConfigs(Iterable<? extends PCFileConfig> elements);
        Builder destinationDir(Path path);
        PCConfig build();
    }
}