package com.jgazula.easyresources.core.enhancedresourcebundle;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Builder
@Value
public class ERBConfig {
    @NonNull String generatedBy;
    @Builder.Default List<ERBBundleConfig> bundleConfigs = new ArrayList<>();
    @NonNull Path destinationDir;
}
