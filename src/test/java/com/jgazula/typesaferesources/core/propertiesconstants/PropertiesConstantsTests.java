package com.jgazula.typesaferesources.core.propertiesconstants;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import com.jgazula.typesaferesources.core.testutil.TestConstants;
import java.nio.file.Paths;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class PropertiesConstantsTests {

  @Test
  public void nullConfigThrowsException() {
    assertThatNullPointerException().isThrownBy(() -> PropertiesConstants.create(null));
  }

  @Test
  public void createNewInstance() {
    // given
    PCFileConfig fileConfig =
        PCFileConfig.builder()
            .generatedPackageName(TestConstants.TEST_PACKAGE_NAME)
            .generatedClassName(TestConstants.TEST_CLASS_NAME)
            .propertiesPath(Paths.get(TestConstants.TEST_PROPERTIES_FILE))
            .build();

    PCConfig config =
        PCConfig.builder()
            .fileConfigs(Collections.singletonList(fileConfig))
            .destinationDir(Paths.get(TestConstants.DESTINATION_DIR))
            .build();
    // when
    PropertiesConstants instance = PropertiesConstants.create(config);

    // then
    assertThat(instance).isNotNull();
  }
}
