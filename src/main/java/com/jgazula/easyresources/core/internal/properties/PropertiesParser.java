package com.jgazula.easyresources.core.internal.properties;

import java.util.regex.Pattern;

/**
 * A helper class which parses and coverts properties keys.
 */
public class PropertiesParser {

    private static final Pattern NON_WORD_AND_UNDERSCORE_PATTERN = Pattern.compile("[\\W_]");
    private static final String UNDERSCORE = "_";

    /**
     * Formats the given key to the style of a static final variable. <br>
     * For example, {@code my.key} becomes {@code MY_KEY}
     */
    public String keyToStaticFinalVariable(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Cannot parse null or empty property key.");
        }

        String[] tokens = NON_WORD_AND_UNDERSCORE_PATTERN.split(key);
        if (tokens.length == 1) {
            return tokens[0].toUpperCase();
        } else {
            var sb = new StringBuilder();
            for (int i = 0; i < tokens.length; i++) {
                sb.append(tokens[i].toUpperCase());
                if ((i + 1) != tokens.length) {
                    sb.append(UNDERSCORE);
                }
            }
            return sb.toString();
        }
    }

    /**
     * Formats the given key to the style of a camel case method name. <br>
     * For example, {@code my.key} becomes {@code myKey}
     */
    public String keyToMethodName(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Cannot parse null or empty property key.");
        }

        String[] tokens = NON_WORD_AND_UNDERSCORE_PATTERN.split(key);
        if (tokens.length == 1) {
            return tokens[0];
        } else {
            var sb = new StringBuilder();
            sb.append(tokens[0]);

            for (int i = 1; i < tokens.length; i++) {
                sb.append(Character.toUpperCase(tokens[i].charAt(0)));
                sb.append(tokens[i].substring(1));
            }
            return sb.toString();
        }
    }
}
