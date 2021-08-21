package com.jgazula.typesaferesources.core.internal.classgeneration;

import static org.assertj.core.api.Assertions.assertThat;

import com.jgazula.typesaferesources.core.testutil.TestConstants;
import com.jgazula.typesaferesources.core.testutil.TestHelper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import spoon.FluentLauncher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.visitor.filter.TypeFilter;

public class PoetClassGeneratorTests {

  @TempDir Path tmpDir;

  @Test
  public void classIsGenerated() throws IOException {
    // given
    ImmutablePoetClassGeneratorConfig config =
        ImmutablePoetClassGeneratorConfig.builder()
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
    ImmutablePoetClassGeneratorConfig config =
        ImmutablePoetClassGeneratorConfig.builder()
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
    ImmutablePoetClassGeneratorConfig config =
        ImmutablePoetClassGeneratorConfig.builder()
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
    List<CtField> fields =
        ctClass.getElements(
            new TypeFilter<CtField>(CtField.class) {
              @Override
              public boolean matches(CtField element) {
                return element
                        .getModifiers()
                        .containsAll(
                            Arrays.asList(
                                ModifierKind.PUBLIC, ModifierKind.STATIC, ModifierKind.FINAL))
                    && element
                        .getType()
                        .getTypeDeclaration()
                        .getSimpleName()
                        .equals(String.class.getSimpleName());
              }
            });

    assertThat(fields).hasSize(constantStringInfo.size());
    for (CtField field : fields) {
      assertThat(constantStringInfo).containsKey(field.getSimpleName());
      String varValue = constantStringInfo.get(field.getSimpleName());

      assertThat(field.getAssignment().getType().getSimpleName())
          .isEqualTo(String.class.getSimpleName());
      assertThat(field.getAssignment().toString()).isEqualTo(String.format("\"%s\"", varValue));
    }
  }

  private CtClass<?> getCtClass(Path generatedFile) {
    return new FluentLauncher()
        .inputResource(generatedFile.toString()).encoding(StandardCharsets.UTF_8)
            .outputDirectory(tmpDir.toFile()).buildModel()
            .getElements(new TypeFilter<>(CtClass.class)).stream()
            .findFirst()
            .orElseThrow(
                () -> new IllegalStateException(generatedFile + " does not contain a class"));
  }
}
