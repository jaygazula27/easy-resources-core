package com.jgazula.easyresources.core.enhancedresourcebundle;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;

@Builder
@Value
public class ERBBundleConfig {
    @NonNull Path bundlePath;
    @NonNull String bundleName;
    @NonNull String generatedClassName;
    @NonNull String generatedPackageName;
}
