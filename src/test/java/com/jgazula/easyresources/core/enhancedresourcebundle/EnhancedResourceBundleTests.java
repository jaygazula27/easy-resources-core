package com.jgazula.easyresources.core.enhancedresourcebundle;

import com.jgazula.easyresources.core.testutil.TestConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class EnhancedResourceBundleTests {

    @Test
    public void nullConfigThrowsException() {
        Assertions.assertThatNullPointerException().isThrownBy(() -> EnhancedResourceBundle.create(null));
    }

    @Test
    public void createNewInstance() {
        // given
        ERBBundleConfig bundleConfig = ERBBundleConfig.builder()
                .generatedPackageName(TestConstants.TEST_PACKAGE_NAME)
                .generatedClassName(TestConstants.TEST_CLASS_NAME)
                .bundleName(TestConstants.TEST_RESOURCE_BUNDLE_NAME)
                .bundlePath(TestConstants.TEST_RESOURCE_BUNDLE_PATH)
                .build();

        ERBConfig config = ERBConfig.builder()
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .bundleConfigs(Collections.singletonList(bundleConfig))
                .destinationDir(Paths.get(TestConstants.DESTINATION_DIR))
                .build();

        // when
        var instance = EnhancedResourceBundle.create(config);

        // then
        assertThat(instance).isNotNull();
    }
}
