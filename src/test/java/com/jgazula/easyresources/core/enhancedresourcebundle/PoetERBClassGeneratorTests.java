package com.jgazula.easyresources.core.enhancedresourcebundle;

import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorConfig;
import com.jgazula.easyresources.core.testutil.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import spoon.FluentLauncher;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtFieldWrite;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PoetERBClassGeneratorTests {

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
        CtClass<?> ctClass = getCtClass(generatedFile);
        assertThatNecessaryClassFieldsWereAdded(ctClass);

        Set<? extends CtConstructor<?>> constructors = ctClass.getConstructors();
        assertThat(constructors).hasSize(1);
        CtConstructor<?> constructor = constructors.iterator().next();

        List<CtParameter<?>> parameters = constructor.getParameters();
        assertThat(parameters).hasSize(1);
        assertThat(parameters.get(0).getType().getTypeDeclaration().getSimpleName()).isEqualTo(ResourceBundle.class.getSimpleName());
        assertThat(parameters.get(0).getSimpleName()).isEqualTo(PoetERBClassGenerator.RESOURCE_BUNDLE_VARIABLE_NAME);

        assertThatNecessaryConstructorAssignmentsWereMade(constructor);
    }

    private void assertThatNecessaryClassFieldsWereAdded(CtClass<?> ctClass) {
        List<CtField<?>> fields = ctClass.getFields();
        assertThat(fields).hasSize(1);

        CtField<?> resourceBundle = fields.get(0);
        assertThat(resourceBundle.getModifiers()).containsExactlyInAnyOrder(ModifierKind.PRIVATE, ModifierKind.FINAL);
        assertThat(resourceBundle.getSimpleName()).isEqualTo(PoetERBClassGenerator.RESOURCE_BUNDLE_VARIABLE_NAME);
    }

    private void assertThatNecessaryConstructorAssignmentsWereMade(CtConstructor<?> constructor) {
        List<CtAssignment<?, ?>> assignments = constructor.getBody().getElements(new TypeFilter<>(CtAssignment.class));
        assertThat(assignments).hasSize(1);

        CtAssignment<?, ?> resourceBundleAssignment = assignments.get(0);
        CtFieldWrite<?> resourceBundleLHS = (CtFieldWrite<?>) resourceBundleAssignment.getAssigned();
        assertThat(resourceBundleLHS.getTarget().getType().getTypeDeclaration().getSimpleName()).isEqualTo(TestConstants.TEST_CLASS_NAME);
        assertThat(resourceBundleLHS.getVariable().getSimpleName()).isEqualTo(PoetERBClassGenerator.RESOURCE_BUNDLE_VARIABLE_NAME);
        CtVariableRead<?> resourceBundleRHS = (CtVariableRead<?>) resourceBundleAssignment.getAssignment();
        assertThat(resourceBundleRHS.getVariable().getSimpleName()).isEqualTo(PoetERBClassGenerator.RESOURCE_BUNDLE_VARIABLE_NAME);
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
