package com.jgazula.easyresources.core.internal.classgeneration;

import com.jgazula.easyresources.core.testutil.TestConstants;
import com.jgazula.easyresources.core.testutil.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import spoon.FluentLauncher;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PoetClassGeneratorTests {

    private static final String RESOURCE_BUNDLE_VARIABLE_NAME = "resourceBundle";
    private static final String DUMMY_STRING_VARIABLE_NAME = "xString";

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
        ClassGenerator classGenerator = new PoetClassGenerator(config);
        Path generatedFile = classGenerator.write(tmpDir);

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
        ClassGenerator classGenerator = new PoetClassGenerator(config);
        Path generatedFile = classGenerator.write(tmpDir);

        // then
        CtClass<?> ctClass = getCtClass(generatedFile);
        assertThat(ctClass.getPackage().getQualifiedName()).isEqualTo(TestConstants.TEST_PACKAGE_NAME);
        assertThat(ctClass.getSimpleName()).isEqualTo(TestConstants.TEST_CLASS_NAME);
    }

    @Test
    public void publicConstantStringsAreAdded() throws IOException {
        // given
        ClassGeneratorConfig config = ClassGeneratorConfig.builder()
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .packageName(TestConstants.TEST_PACKAGE_NAME)
                .className(TestConstants.TEST_CLASS_NAME)
                .build();

        Map<String, String> constantStringInfo = TestHelper.generateProperties();

        // when
        ClassGenerator classGenerator = new PoetClassGenerator(config);
        constantStringInfo.forEach(classGenerator::addPublicConstantString);
        Path generatedFile = classGenerator.write(tmpDir);

        // then
        CtClass<?> ctClass = getCtClass(generatedFile);
        List<CtField<?>> fields = ctClass.getFields();

        assertThat(fields).hasSize(constantStringInfo.size());

        for (CtField<?> field : fields) {
            assertThat(constantStringInfo).containsKey(field.getSimpleName());
            String varValue = constantStringInfo.get(field.getSimpleName());

            assertThat(field.getModifiers()).containsExactlyInAnyOrder(ModifierKind.PUBLIC, ModifierKind.STATIC, ModifierKind.FINAL);
            assertThat(field.getAssignment().getType().getSimpleName()).isEqualTo(String.class.getSimpleName());
            assertThat(field.getAssignment().toString()).isEqualTo(String.format("\"%s\"", varValue));
        }
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
        ClassGenerator classGenerator = new PoetClassGenerator(config);
        classGenerator.addPrivateFinalField(ResourceBundle.class, RESOURCE_BUNDLE_VARIABLE_NAME);
        Path generatedFile = classGenerator.write(tmpDir);

        // then
        CtClass<?> ctClass = getCtClass(generatedFile);
        List<CtField<?>> fields = ctClass.getFields();

        assertThat(fields).hasSize(1);
        CtField<?> field = fields.get(0);
        assertThat(field.getModifiers()).containsExactlyInAnyOrder(ModifierKind.PRIVATE, ModifierKind.FINAL);
        assertThat(field.getSimpleName()).isEqualTo(RESOURCE_BUNDLE_VARIABLE_NAME);
    }

    @Test
    public void allArgsConstructor() throws IOException {
        // given
        ClassGeneratorConfig config = ClassGeneratorConfig.builder()
                .generatedBy(TestConstants.TEST_PLUGIN_NAME)
                .packageName(TestConstants.TEST_PACKAGE_NAME)
                .className(TestConstants.TEST_CLASS_NAME)
                .build();

        // when
        ClassGenerator classGenerator = new PoetClassGenerator(config);
        classGenerator.addPrivateFinalField(ResourceBundle.class, RESOURCE_BUNDLE_VARIABLE_NAME);
        classGenerator.addPrivateFinalField(String.class, DUMMY_STRING_VARIABLE_NAME);
        classGenerator.addConstructorWithArgs(Map.of(ResourceBundle.class, RESOURCE_BUNDLE_VARIABLE_NAME,
                String.class, DUMMY_STRING_VARIABLE_NAME));

        Path generatedFile = classGenerator.write(tmpDir);

        // then
        CtClass<?> ctClass = getCtClass(generatedFile);
        Set<? extends CtConstructor<?>> constructors = ctClass.getConstructors();
        assertThat(constructors).hasSize(1);

        CtConstructor<?> constructor = constructors.iterator().next();

        List<CtParameter<?>> parameters = constructor.getParameters();
        assertThat(parameters).hasSize(2);
        parameters.sort(Comparator.comparing(CtParameter::getSimpleName)); // sometimes out of order, so sort it to make testing simpler
        assertThat(parameters.get(0).getType().getTypeDeclaration().getSimpleName()).isEqualTo(ResourceBundle.class.getSimpleName());
        assertThat(parameters.get(0).getSimpleName()).isEqualTo(RESOURCE_BUNDLE_VARIABLE_NAME);
        assertThat(parameters.get(1).getType().getTypeDeclaration().getSimpleName()).isEqualTo(String.class.getSimpleName());
        assertThat(parameters.get(1).getSimpleName()).isEqualTo(DUMMY_STRING_VARIABLE_NAME);

        List<CtAssignment<?, ?>> assignments = constructor.getBody().getElements(new TypeFilter<>(CtAssignment.class));
        assignments.sort(Comparator.comparing(CtAssignment::toString)); // sometimes out of order, so sort it to make testing simpler
        assertThat(assignments.get(0).toString()).isEqualTo(String.format("this.%s = %s", RESOURCE_BUNDLE_VARIABLE_NAME, RESOURCE_BUNDLE_VARIABLE_NAME));
        assertThat(assignments.get(1).toString()).isEqualTo(String.format("this.%s = %s", DUMMY_STRING_VARIABLE_NAME, DUMMY_STRING_VARIABLE_NAME));
    }

    private CtClass<?> getCtClass(Path generatedFile) {
        return new FluentLauncher()
                .inputResource(generatedFile.toString())
                .encoding(StandardCharsets.UTF_8)
                .outputDirectory(tmpDir.toFile())
                .buildModel()
                .getElements(new TypeFilter<>(CtClass.class))
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(generatedFile + " does not contain a class"));
    }
}
