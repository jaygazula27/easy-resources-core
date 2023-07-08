package com.jgazula.easyresources.core.internal.classgeneration;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class ClassGeneratorConfig {
    @NonNull String generatedBy;
    @NonNull String packageName;
    @NonNull String className;
}
