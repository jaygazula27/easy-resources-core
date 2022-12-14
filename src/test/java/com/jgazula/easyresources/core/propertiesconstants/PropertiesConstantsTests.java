package com.jgazula.easyresources.core.propertiesconstants;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import com.jgazula.easyresources.core.testutil.TestConstants;

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
        PropertiesConstantsFileConfig fileConfig = PropertiesConstantsFileConfig.builder()
                .generatedPackageName(TestConstants.TEST_PACKAGE_NAME)
                .generatedClassName(TestConstants.TEST_CLASS_NAME)
                .propertiesPath(Paths.get(TestConstants.TEST_PROPERTIES_FILE))
                .build();

        PropertiesConstantsConfig config = PropertiesConstantsConfig.builder()
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .fileConfigs(Collections.singletonList(fileConfig))
                .destinationDir(Paths.get(TestConstants.DESTINATION_DIR))
                .build();
        // when
        PropertiesConstants instance = PropertiesConstants.create(config);

        // then
        assertThat(instance).isNotNull();
    }
}
