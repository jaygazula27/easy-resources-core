package com.jgazula.easyresources.core.propertiesconstants;

import com.jgazula.easyresources.core.internal.util.ImmutableStyle;

import java.nio.file.Path;
import java.util.List;

import org.immutables.value.Value;

@ImmutableStyle
@Value.Immutable
public interface PropertiesConstantsConfig {

    List<PropertiesConstantsFileConfig> fileConfigs();

    Path destinationDir();

    static Builder builder() {
        return ImmutablePropertiesConstantsConfig.builder();
    }

    interface Builder {

        Builder fileConfigs(Iterable<? extends PropertiesConstantsFileConfig> elements);

        Builder destinationDir(Path path);

        PropertiesConstantsConfig build();
    }
}
