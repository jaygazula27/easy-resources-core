package com.jgazula.easyresources.core.internal.classgeneration;

import com.jgazula.easyresources.core.internal.util.ImmutableStyle;
import org.immutables.value.Value;

@ImmutableStyle
@Value.Immutable
public interface PoetClassGeneratorConfig {

    String generatedBy();

    String packageName();

    String className();
}
