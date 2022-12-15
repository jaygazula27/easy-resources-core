package com.jgazula.easyresources.core.testutil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TestHelper {

    private static final String ALPHABETS = "abcdefghijklmnopqrstuvwxyz";
    private static final int RANDOM_ALPHABETIC_LENGTH = 10;
    private static final int NUM_OF_PROPERTIES = 3;

    public static String randomAlphabetic() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < RANDOM_ALPHABETIC_LENGTH; i++) {
            sb.append(ALPHABETS.charAt(random.nextInt(ALPHABETS.length())));
        }

        return sb.toString();
    }

    public static Map<String, String> generateProperties() {
        Map<String, String> properties = new HashMap<>(NUM_OF_PROPERTIES);
        for (int i = 0; i < NUM_OF_PROPERTIES; i++) {
            properties.put(TestHelper.randomAlphabetic(), TestHelper.randomAlphabetic());
        }
        return Collections.unmodifiableMap(properties);
    }
}
