package com.jgazula.easyresources.core.enhancedresourcebundle;

import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorConfig;
import com.jgazula.easyresources.core.testutil.TestConstants;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import spoon.FluentLauncher;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtFieldWrite;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtBlockImpl;
import spoon.support.reflect.code.CtInvocationImpl;
import spoon.support.reflect.code.CtStatementImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PoetERBClassGeneratorTests {

    private static final String TEST_KEY = "my.test.key";
    private static final String TEST_KEY_METHOD_NAME = "myTestKey";

    @TempDir
    Path tmpDir;

    @Disabled
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

    @Disabled
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
        CtClass<?> ctClass = getCtClass(generatedFile);

        List<CtMethod<?>> methods = ctClass.getMethodsByName(TEST_KEY_METHOD_NAME);
        assertThat(methods).hasSize(1);

        CtMethod<?> method = methods.get(0);
        assertThat(method.getModifiers()).containsExactly(ModifierKind.PUBLIC);
        assertThat(method.getType().getSimpleName()).isEqualTo(String.class.getSimpleName());
        assertThat(method.getSimpleName()).isEqualTo(TEST_KEY_METHOD_NAME);
        assertThat(method.getParameters()).isEmpty();
        assertThat(method.getThrownTypes()).isEmpty();

        List<CtBlockImpl<?>> blocks = method.getBody().getElements(new TypeFilter<>(CtBlockImpl.class));
        assertThat(blocks).hasSize(1);

        List<CtStatement> statements = blocks.get(0).getStatements();
        assertThat(statements).hasSize(2);

        CtStatement firstStatement = statements.get(0);
        assertThat(firstStatement).isOfAnyClassIn(CtInvocationImpl.class);
        CtAssignment<?, ?> firstStatementAssignment = firstStatement
                .getElements(new TypeFilter<>(CtAssignment.class)).get(0);
        System.out.println(firstStatementAssignment);

//        List<CtStatement> statements = ctBlock.getStatements();
//        assertThat(statements).hasSize(2);
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
