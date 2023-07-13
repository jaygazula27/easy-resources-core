package com.jgazula.easyresources.core.internal.classgeneration;

import com.jgazula.easyresources.core.enhancedresourcebundle.ERBClassGenerator;
import com.jgazula.easyresources.core.testutil.TestConstants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassGeneratorFactoryTests {

    @Test
    public void classGeneratorInstanceIsCreated() {
        // given
        ClassGeneratorConfig config = ClassGeneratorConfig.builder()
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .packageName(TestConstants.TEST_PACKAGE_NAME)
                .className(TestConstants.TEST_CLASS_NAME)
                .build();

        // when
        ClassGenerator generator = new ClassGeneratorFactory().getGenerator(config);

        // then
        assertThat(generator).isNotNull();
    }

    @Test
    public void erbClassGeneratorInstanceIsCreated() {
        // given
        ClassGeneratorConfig config = ClassGeneratorConfig.builder()
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .packageName(TestConstants.TEST_PACKAGE_NAME)
                .className(TestConstants.TEST_CLASS_NAME)
                .build();

        // when
        ERBClassGenerator generator = new ClassGeneratorFactory().getERBClassGenerator(config);

        // then
        assertThat(generator).isNotNull();
    }
}
