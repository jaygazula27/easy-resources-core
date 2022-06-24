package com.jgazula.typesaferesources.core.internal.classgeneration;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This implementation generates a Java file using the JavaPoet library.
 */
public class PoetClassGenerator implements ClassGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoetClassGenerator.class);
    private static final String INDENT = "    ";

    private final PoetClassGeneratorConfig config;
    private final List<FieldSpec> fieldSpecs = new ArrayList<>();

    public PoetClassGenerator(PoetClassGeneratorConfig config) {
        this.config = config;
    }

    @Override
    public void addPublicConstantString(String variableName, String variableValue) {
        var fieldSpec = FieldSpec.builder(String.class, variableName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$S", variableValue)
                .build();
        fieldSpecs.add(fieldSpec);
    }

    @Override
    public Path write(Path directory) throws IOException {
        var typeSpec = TypeSpec.classBuilder(config.className())
                .addModifiers(Modifier.PUBLIC)
                .addFields(fieldSpecs)
                .build();

        Path outputPath = JavaFile.builder(config.packageName(), typeSpec)
                .indent(INDENT)
                .build()
                .writeToPath(directory, StandardCharsets.UTF_8);
        LOGGER.debug("Wrote Java file for {}.{} to {}", config.packageName(), config.className(), outputPath);

        return outputPath;
    }
}
