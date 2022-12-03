package com.jgazula.easyresources.core.propertiesconstants;

import com.jgazula.easyresources.core.internal.util.ImmutableStyle;

import java.nio.file.Path;
import java.util.List;

import org.immutables.value.Value;

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
