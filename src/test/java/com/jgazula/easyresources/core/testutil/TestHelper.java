package com.jgazula.easyresources.core.testutil;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class TestHelper {

    private static final String ALPHABETS = "abcdefghijklmnopqrstuvwxyz";
    private static final int RANDOM_ALPHABETIC_LENGTH = 10;
    private static final int NUM_OF_PROPERTIES = 3;
    private static final String COMMENT_PREFIX = "//";

    private TestHelper() { }

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

    public static Path getTestResourcePath(String resourceDir, String resourceName) {
        try {
            var resource = File.separator + resourceDir + File.separator + resourceName;
            return Paths.get(Objects.requireNonNull(TestHelper.class.getResource(resource)).toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean generatedFileMatchesExpected(Path generated, Path expected) {
        try (var generatedReader = Files.newBufferedReader(generated, StandardCharsets.UTF_8);
             var expectedReader = Files.newBufferedReader(expected, StandardCharsets.UTF_8)) {

            // account for the special cases where the first two lines are comments

            // first line is a comment which we expect both files to have an exact match
            var generatedFirstLine = generatedReader.readLine();
            var expectedFirstLine = expectedReader.readLine();
            var firstLineEqual = Objects.equals(generatedFirstLine, expectedFirstLine);
            if (!firstLineEqual) {
                return false;
            }

            // we only need to check that the second line is a comment
            // since the comment is a dynamic timestamp
            var generatedSecondLine = generatedReader.readLine();
            var expectedSecondLine = expectedReader.readLine();
            if (generatedSecondLine == null ||
                    expectedSecondLine == null ||
                    !generatedSecondLine.startsWith(COMMENT_PREFIX) ||
                    !expectedSecondLine.startsWith(COMMENT_PREFIX)) {
                throw new IllegalStateException("Timestamp comment is always expected");
            }

            while (true) {
                var generatedLine = generatedReader.readLine();
                var expectedLine = expectedReader.readLine();

                if (generatedLine == null && expectedLine == null) {
                    // end of both files
                    break;
                } else if (!Objects.equals(generatedLine, expectedLine)) {
                    return false;
                }
            }

            return true;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
