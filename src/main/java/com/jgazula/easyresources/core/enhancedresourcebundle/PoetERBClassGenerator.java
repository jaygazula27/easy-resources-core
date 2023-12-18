package com.jgazula.easyresources.core.enhancedresourcebundle;

import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorConfig;
import com.jgazula.easyresources.core.internal.classgeneration.ClassGeneratorVariable;
import com.jgazula.easyresources.core.internal.classgeneration.PoetClassGenerator;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This implementation generates an enhanced resource bundle Java file using the JavaPoet library.
 */
public class PoetERBClassGenerator extends PoetClassGenerator implements ERBClassGenerator {

    static final String RESOURCE_BUNDLE_VARIABLE_NAME = "resourceBundle";
    private static final String ARGUMENT_NAME = "arg";
    private static final String MESSAGE_VARIABLE_NAME = "message";
    private static final String MESSAGE_ARGUMENTS_VARIABLE_NAME = "messageArguments";

    public PoetERBClassGenerator(ClassGeneratorConfig config) {
        super(config);
    }

    @Override
    public void initialize() {
        var resourceBundleVar = new ClassGeneratorVariable(ResourceBundle.class, RESOURCE_BUNDLE_VARIABLE_NAME);
        addPrivateFinalField(resourceBundleVar);
        addConstructorWithArgs(List.of(resourceBundleVar));
    }

    @Override
    public void addMethod(String key, String name, List<Type> argTypes) {
        List<ParameterSpec> params = IntStream.range(0, argTypes.size())
                .mapToObj(i -> ParameterSpec.builder(argTypes.get(i), ARGUMENT_NAME + i).build())
                .collect(Collectors.toList());

        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addParameters(params)
                .addStatement("$T $N = this.$N.getString($S)", String.class, MESSAGE_VARIABLE_NAME,
                        RESOURCE_BUNDLE_VARIABLE_NAME, key);

        if (params.isEmpty()) {
            builder.addStatement("return $N", MESSAGE_VARIABLE_NAME);
        } else {
            List<CodeBlock> args = params.stream()
                    .map(param -> CodeBlock.of("$L", param.name))
                    .collect(Collectors.toList());

            builder.addStatement("$T $N = {$L}", Object[].class, MESSAGE_ARGUMENTS_VARIABLE_NAME,
                            CodeBlock.join(args, ", "))
                    .addStatement("return new $T($N, this.$N.getLocale()).format($N)", MessageFormat.class,
                            MESSAGE_VARIABLE_NAME, RESOURCE_BUNDLE_VARIABLE_NAME, MESSAGE_ARGUMENTS_VARIABLE_NAME);
        }

        MethodSpec methodSpec = builder.build();
        addMethodSpec(methodSpec);
    }
}
