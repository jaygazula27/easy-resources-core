// This class was auto generated by easy-resources-maven-plugin.
// October 7, 2023 at 4:08:33 PM EST
package com.jgazula.test;

import java.lang.String;
import java.util.ResourceBundle;

public class TestClass {
    private final ResourceBundle resourceBundle;

    public TestClass(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public String myTestKey() {
        String message = this.resourceBundle.getString("my.test.key");
        return message;
    }
}