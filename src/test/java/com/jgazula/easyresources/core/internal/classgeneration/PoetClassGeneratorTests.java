package com.jgazula.easyresources.core.internal.classgeneration;

import com.jgazula.easyresources.core.testutil.TestConstants;
import com.jgazula.easyresources.core.testutil.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

import static org.assertj.core.api.Assertions.assertThat;

public class PoetClassGeneratorTests {

    private static final String DUMMY_CONSTANT_STRING_FIRST_VAR = "FIRST_STRING_CONSTANT_KEY";
    private static final String DUMMY_CONSTANT_STRING_FIRST_VALUE = "firstStringConstantValue";
    private static final String DUMMY_CONSTANT_STRING_SECOND_VAR = "SECOND_STRING_CONSTANT_KEY";
    private static final String DUMMY_CONSTANT_STRING_SECOND_VALUE = "secondStringConstantValue";
    private static final String DUMMY_CONSTANT_STRING_THIRD_VAR = "THIRD_STRING_CONSTANT_KEY";
    private static final String DUMMY_CONSTANT_STRING_THIRD_VALUE = "thirdStringConstantValue";

    private static final Type DUMMY_PRIVATE_FIRST_VAR_TYPE = String.class;
    private static final String DUMMY_PRIVATE_FIRST_VAR = "firstPrivateVar";
    private static final Type DUMMY_PRIVATE_SECOND_VAR_TYPE = ResourceBundle.class;
    private static final String DUMMY_PRIVATE_SECOND_VAR = "secondPrivateVar";
    private static final Type DUMMY_PRIVATE_THIRD_VAR_TYPE = BigDecimal.class;
    private static final String DUMMY_PRIVATE_THIRD_VAR = "thirdPrivateVar";
    private static final Type DUMMY_PRIVATE_FOURTH_VAR_TYPE = String.class;
    private static final String DUMMY_PRIVATE_FOURTH_VAR = "fourthPrivateVar";

    private static final String RESOURCES_DIR = "poetclassgeneratortests";

    @TempDir
    Path tmpDir;

    @Test
    public void classIsGenerated() throws IOException {
        // given
        ClassGeneratorConfig config = ClassGeneratorConfig.builder()
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .packageName(TestConstants.TEST_PACKAGE_NAME)
                .className(TestConstants.TEST_CLASS_NAME)
                .build();

        // when
        Path generatedFile = new PoetClassGenerator(config).write(tmpDir);

        // then
        assertThat(generatedFile).exists().isRegularFile();
    }

    @Test
    public void packageNameAndClassNameIsCorrect() throws IOException {
        // given
        ClassGeneratorConfig config = ClassGeneratorConfig.builder()
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .packageName(TestConstants.TEST_PACKAGE_NAME)
                .className(TestConstants.TEST_CLASS_NAME)
                .build();

        // when
        Path generatedFile = new PoetClassGenerator(config).write(tmpDir);

        // then
        var expectedFile = TestHelper.getTestResourcePath(RESOURCES_DIR, "PackageNameAndClassNameIsCorrect.java");
        assertThat(TestHelper.generatedFileMatchesExpected(generatedFile, expectedFile)).isTrue();
    }

    @Test
    public void publicConstantStringsAreAdded() throws IOException {
        // given
        ClassGeneratorConfig config = ClassGeneratorConfig.builder()
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .packageName(TestConstants.TEST_PACKAGE_NAME)
                .className(TestConstants.TEST_CLASS_NAME)
                .build();

        // when
        Path generatedFile = new PoetClassGenerator(config)
                .addPublicConstantString(DUMMY_CONSTANT_STRING_FIRST_VAR, DUMMY_CONSTANT_STRING_FIRST_VALUE)
                .addPublicConstantString(DUMMY_CONSTANT_STRING_SECOND_VAR, DUMMY_CONSTANT_STRING_SECOND_VALUE)
                .addPublicConstantString(DUMMY_CONSTANT_STRING_THIRD_VAR, DUMMY_CONSTANT_STRING_THIRD_VALUE)
                .write(tmpDir);

        // then
        var expectedFile = TestHelper.getTestResourcePath(RESOURCES_DIR, "PublicConstantStringsAreAdded.java");
        assertThat(TestHelper.generatedFileMatchesExpected(generatedFile, expectedFile)).isTrue();
    }

    @Test
    public void privateFinalVariableIsAdded() throws IOException {
        // given
        ClassGeneratorConfig config = ClassGeneratorConfig.builder()
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .packageName(TestConstants.TEST_PACKAGE_NAME)
                .className(TestConstants.TEST_CLASS_NAME)
                .build();

        // when
        Path generatedFile = new PoetClassGenerator(config)
                .addPrivateFinalField(new ClassGeneratorVariable(DUMMY_PRIVATE_FIRST_VAR_TYPE, DUMMY_PRIVATE_FIRST_VAR))
                .addPrivateFinalField(new ClassGeneratorVariable(DUMMY_PRIVATE_SECOND_VAR_TYPE, DUMMY_PRIVATE_SECOND_VAR))
                .addPrivateFinalField(new ClassGeneratorVariable(DUMMY_PRIVATE_THIRD_VAR_TYPE, DUMMY_PRIVATE_THIRD_VAR))
                .addPrivateFinalField(new ClassGeneratorVariable(DUMMY_PRIVATE_FOURTH_VAR_TYPE, DUMMY_PRIVATE_FOURTH_VAR))
                .write(tmpDir);

        // then
        var expectedFile = TestHelper.getTestResourcePath(RESOURCES_DIR, "PrivateFinalVariableIsAdded.java");
        assertThat(TestHelper.generatedFileMatchesExpected(generatedFile, expectedFile)).isTrue();
    }

    @Test
    public void allArgsConstructor() throws IOException {
        // given
        ClassGeneratorConfig config = ClassGeneratorConfig.builder()
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .packageName(TestConstants.TEST_PACKAGE_NAME)
                .className(TestConstants.TEST_CLASS_NAME)
                .build();

        var firstPrivateVar = new ClassGeneratorVariable(DUMMY_PRIVATE_FIRST_VAR_TYPE, DUMMY_PRIVATE_FIRST_VAR);
        var secondPrivateVar = new ClassGeneratorVariable(DUMMY_PRIVATE_SECOND_VAR_TYPE, DUMMY_PRIVATE_SECOND_VAR);
        var thirdPrivateVar = new ClassGeneratorVariable(DUMMY_PRIVATE_THIRD_VAR_TYPE, DUMMY_PRIVATE_THIRD_VAR);
        var fourthPrivateVar = new ClassGeneratorVariable(DUMMY_PRIVATE_FOURTH_VAR_TYPE, DUMMY_PRIVATE_FOURTH_VAR);

        // when
        Path generatedFile = new PoetClassGenerator(config)
                .addPrivateFinalField(firstPrivateVar)
                .addPrivateFinalField(secondPrivateVar)
                .addPrivateFinalField(thirdPrivateVar)
                .addPrivateFinalField(fourthPrivateVar)
                .addConstructorWithArgs(List.of(firstPrivateVar, secondPrivateVar, thirdPrivateVar, fourthPrivateVar))
                .write(tmpDir);

        // then
        var expectedFile = TestHelper.getTestResourcePath(RESOURCES_DIR, "AllArgsConstructor.java");
        assertThat(TestHelper.generatedFileMatchesExpected(generatedFile, expectedFile)).isTrue();
    }
}
