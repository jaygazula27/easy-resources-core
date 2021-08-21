package com.jgazula.typesaferesources.core.internal.classgeneration;

import com.jgazula.typesaferesources.core.internal.util.ImmutableStyle;
import org.immutables.value.Value;

@ImmutableStyle
@Value.Immutable
public interface PoetClassGeneratorConfig {

  String packageName();

  String className();
}
