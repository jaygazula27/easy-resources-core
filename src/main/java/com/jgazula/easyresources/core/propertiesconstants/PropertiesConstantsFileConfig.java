package com.jgazula.easyresources.core.propertiesconstants;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;

@Builder
@Value
public class PropertiesConstantsFileConfig {
    @NonNull Path propertiesPath;
    @NonNull String generatedClassName;
    @NonNull String generatedPackageName;
}
