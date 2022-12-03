package com.jgazula.easyresources.core.util;

/**
 * This exception indicates a validation failure.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message, String... args) {
        super(String.format(message, (Object[]) args));
    }
}
