package com.jgazula.easyresources.core.propertiesconstants;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Builder
@Value
public class PropertiesConstantsConfig {
    @NonNull String generatedBy;
    @Builder.Default List<PropertiesConstantsFileConfig> fileConfigs = new ArrayList<>();
    @NonNull Path destinationDir;
}
