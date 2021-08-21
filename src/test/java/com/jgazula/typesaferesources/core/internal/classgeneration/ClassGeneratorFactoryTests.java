package com.jgazula.typesaferesources.core.internal.classgeneration;

import static org.assertj.core.api.Assertions.assertThat;

import com.jgazula.typesaferesources.core.testutil.TestConstants;
import org.junit.jupiter.api.Test;

public class ClassGeneratorFactoryTests {

  @Test
  public void poetGeneratorInstanceIsCreated() {
    // given
    PoetClassGeneratorConfig config =
        ImmutablePoetClassGeneratorConfig.builder()
            .packageName(TestConstants.TEST_PACKAGE_NAME)
            .className(TestConstants.TEST_CLASS_NAME)
            .build();

    // when
    ClassGenerator generator = new ClassGeneratorFactory().getGenerator(config);

    // then
    assertThat(generator).isNotNull().isInstanceOf(PoetClassGenerator.class);
  }
}
