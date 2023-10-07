package com.jgazula.easyresources.core.enhancedresourcebundle;

import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorConfig;
import com.jgazula.easyresources.core.testutil.TestConstants;
import com.jgazula.easyresources.core.testutil.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class PoetERBClassGeneratorTests {

    private static final String TEST_KEY = "my.test.key";
    private static final String TEST_KEY_METHOD_NAME = "myTestKey";
    private static final String RESOURCES_DIR = "poeterbclassgeneratortests";

    @TempDir
    Path tmpDir;

    @Test
    public void initializedWithConstructorAndClassFields() throws IOException {
        // given
        ClassGeneratorConfig config = ClassGeneratorConfig.builder()
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .packageName(TestConstants.TEST_PACKAGE_NAME)
                .className(TestConstants.TEST_CLASS_NAME)
                .build();

        // when
        ERBClassGenerator classGenerator = new PoetERBClassGenerator(config);
        classGenerator.initialize();
        Path generatedFile = classGenerator.write(tmpDir);

        // then
        var expectedFile = TestHelper.getTestResourcePath(RESOURCES_DIR, "InitializedWithConstructorAndClassFields.java");
        assertThat(TestHelper.generatedFileMatchesExpected(generatedFile, expectedFile)).isTrue();
    }

    @Test
    public void addMethodWithNoArgs() throws IOException {
        // given
        ClassGeneratorConfig config = ClassGeneratorConfig.builder()
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .packageName(TestConstants.TEST_PACKAGE_NAME)
                .className(TestConstants.TEST_CLASS_NAME)
                .build();

        // when
        ERBClassGenerator classGenerator = new PoetERBClassGenerator(config);
        classGenerator.addMethod(TEST_KEY, TEST_KEY_METHOD_NAME, Collections.emptyList());
        Path generatedFile = classGenerator.write(tmpDir);

        // then
        var expectedFile = TestHelper.getTestResourcePath(RESOURCES_DIR, "AddMethodWithNoArgs.java");
        assertThat(TestHelper.generatedFileMatchesExpected(generatedFile, expectedFile)).isTrue();
    }
}
