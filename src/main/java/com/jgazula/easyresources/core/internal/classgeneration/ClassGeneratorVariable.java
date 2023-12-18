package com.jgazula.easyresources.core.internal.classgeneration;

import lombok.NonNull;
import lombok.Value;

import java.lang.reflect.Type;

@Value
public class ClassGeneratorVariable {
    @NonNull Type type;
    @NonNull String name;
}
