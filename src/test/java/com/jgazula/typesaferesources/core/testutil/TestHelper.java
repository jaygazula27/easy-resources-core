package com.jgazula.typesaferesources.core.testutil;

import java.util.Random;

public class TestHelper {

    private static final String ALPHABETS = "abcdefghijklmnopqrstuvwxyz";
    private static final int RANDOM_ALPHABETIC_LENGTH = 10;

    public static String randomAlphabetic() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < RANDOM_ALPHABETIC_LENGTH; i++) {
            sb.append(ALPHABETS.charAt(random.nextInt(ALPHABETS.length())));
        }

        return sb.toString();
    }
}
