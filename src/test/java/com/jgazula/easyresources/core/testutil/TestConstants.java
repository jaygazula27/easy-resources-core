package com.jgazula.easyresources.core.testutil;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestConstants {

    public static final String TEST_PLUGIN_NAME = "easy-resources-maven-plugin";

    public static final String TEST_PACKAGE_NAME = "com.jgazula.test";
    public static final String TEST_PACKAGE_NAME2 = "com.jgazula.test2";
    public static final String TEST_PACKAGE_NAME3 = "com.jgazula.test3";

    public static final String TEST_CLASS_NAME = "TestClass";
    public static final String TEST_CLASS_NAME2 = "TestClass2";
    public static final String TEST_CLASS_NAME3 = "TestClass3";

    public static final String TEST_PROPERTIES_FILE = "Test.properties";
    public static final String TEST_PROPERTIES_FILE2 = "Test2.properties";
    public static final String TEST_PROPERTIES_FILE3 = "Test3.properties";

    public static final String TEST_RESOURCE_BUNDLE_NAME = "TestResources";
    public static final Path TEST_RESOURCE_BUNDLE_PATH = Paths.get("src", "main", "resources");

    public static final String DESTINATION_DIR = "dest-dir";
}
