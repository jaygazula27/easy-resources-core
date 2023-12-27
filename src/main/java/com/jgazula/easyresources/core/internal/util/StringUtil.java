package com.jgazula.easyresources.core.internal.util;

/**
 * A utility class to simplify String related operations
 */
public class StringUtil {

    private StringUtil() { }

    /**
     * Checks if the given string is null or if it is {@link String#isEmpty()}
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
