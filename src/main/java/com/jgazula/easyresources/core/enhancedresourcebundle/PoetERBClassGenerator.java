package com.jgazula.easyresources.core.enhancedresourcebundle;

import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorConfig;
import com.jgazula.easyresources.core.internal.classgeneration.PoetClassGenerator;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * This implementation generates an enhanced resource bundle Java file using the JavaPoet library.
 */
public class PoetERBClassGenerator extends PoetClassGenerator implements ERBClassGenerator {

    static final String RESOURCE_BUNDLE_VARIABLE_NAME = "resourceBundle";
    static final String MESSAGE_FORMAT_VARIABLE_NAME = "messageFormatter";

    public PoetERBClassGenerator(ClassGeneratorConfig config) {
        super(config);
    }

    @Override
    public void initialize() {
        addPrivateFinalField(ResourceBundle.class, RESOURCE_BUNDLE_VARIABLE_NAME);
        addPrivateFinalField(MessageFormat.class, MESSAGE_FORMAT_VARIABLE_NAME);

        MethodSpec methodSpec = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ResourceBundle.class, RESOURCE_BUNDLE_VARIABLE_NAME)
                .addStatement("this.$N = $N", RESOURCE_BUNDLE_VARIABLE_NAME, RESOURCE_BUNDLE_VARIABLE_NAME)
                .addCode("$L", "\n")
                .addStatement("this.$N = new $T(\"\")", MESSAGE_FORMAT_VARIABLE_NAME, MessageFormat.class)
                .addStatement("this.$N.setLocale(this.$N.getLocale())", MESSAGE_FORMAT_VARIABLE_NAME, RESOURCE_BUNDLE_VARIABLE_NAME)
                .build();
        addMethodSpec(methodSpec);
    }
}
