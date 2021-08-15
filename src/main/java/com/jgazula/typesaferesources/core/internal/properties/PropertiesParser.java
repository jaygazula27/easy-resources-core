package com.jgazula.typesaferesources.core.internal.properties;

import java.util.regex.Pattern;

public class PropertiesParser {

    private static final Pattern NON_WORD_AND_UNDERSCORE_PATTERN = Pattern.compile("[\\W_]");
    private static final String UNDERSCORE = "_";

    public String keyToStaticFinalVariable(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Cannot parse null or empty property key.");
        }

        String[] tokens = NON_WORD_AND_UNDERSCORE_PATTERN.split(key);
        if (tokens.length == 1) {
            return tokens[0].toUpperCase();
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tokens.length; i++) {
                sb.append(tokens[i].toUpperCase());
                if ((i + 1) != tokens.length) {
                    sb.append(UNDERSCORE);
                }
            }
            return sb.toString();
        }
    }
}
