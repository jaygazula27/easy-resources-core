package com.jgazula.easyresources.core.internal.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PropertiesParserTests {

    private static final String ONE_WORD_PROPERTY_KEY = "key";
    private static final String ONE_WORD_PROPERTY_KEY_VARIABLE = "KEY";
    private static final String ONE_WORD_PROPERTY_KEY_METHOD_NAME = "key";
    private static final String TWO_WORDS_PROPERTY_KEY = "my.key";
    private static final String TWO_WORDS_PROPERTY_KEY_VARIABLE = "MY_KEY";
    private static final String TWO_WORDS_PROPERTY_KEY_METHOD_NAME = "myKey";
    private static final String OTHER_CHARS_PROPERTY_KEY = "my-key@with_other.chars";
    private static final String OTHER_CHARS_PROPERTY_KEY_VARIABLE = "MY_KEY_WITH_OTHER_CHARS";
    private static final String OTHER_CHARS_PROPERTY_KEY_METHOD_NAME = "myKeyWithOtherChars";

    private PropertiesParser parser;

    @BeforeEach
    void setUp() {
        parser = new PropertiesParser();
    }

    @Test
    void cannotParseNullAsStaticFinalVariable() {
        assertThatIllegalArgumentException().isThrownBy(() -> parser.keyToStaticFinalVariable(null));
    }

    @Test
    void cannotParseEmptyStrAsStaticFinalVariable() {
        assertThatIllegalArgumentException().isThrownBy(() -> parser.keyToStaticFinalVariable(""));
    }

    @Test
    void parseOneWordAsStaticFinalVariable() {
        assertThat(parser.keyToStaticFinalVariable(ONE_WORD_PROPERTY_KEY))
                .isEqualTo(ONE_WORD_PROPERTY_KEY_VARIABLE);
    }

    @Test
    void parseMultipleWordsAsStaticFinalVariable() {
        assertThat(parser.keyToStaticFinalVariable(TWO_WORDS_PROPERTY_KEY))
                .isEqualTo(TWO_WORDS_PROPERTY_KEY_VARIABLE);
    }

    @Test
    void parseKeyWithHyphensAndOtherCharsAsStaticFinalVariable() {
        assertThat(parser.keyToStaticFinalVariable(OTHER_CHARS_PROPERTY_KEY))
                .isEqualTo(OTHER_CHARS_PROPERTY_KEY_VARIABLE);
    }

    @Test
    void cannotParseNullAsMethodName() {
        assertThatIllegalArgumentException().isThrownBy(() -> parser.keyToMethodName(null));
    }

    @Test
    void cannotParseEmptyStrAsMethodName() {
        assertThatIllegalArgumentException().isThrownBy(() -> parser.keyToMethodName(""));
    }

    @Test
    void parseOneWordAsMethodName() {
        assertThat(parser.keyToMethodName(ONE_WORD_PROPERTY_KEY))
                .isEqualTo(ONE_WORD_PROPERTY_KEY_METHOD_NAME);
    }

    @Test
    void parseMultipleWordsAsMethodName() {
        assertThat(parser.keyToMethodName(TWO_WORDS_PROPERTY_KEY))
                .isEqualTo(TWO_WORDS_PROPERTY_KEY_METHOD_NAME);
    }

    @Test
    void parseKeyWithHyphensAndOtherCharsAsMethodName() {
        assertThat(parser.keyToMethodName(OTHER_CHARS_PROPERTY_KEY))
                .isEqualTo(OTHER_CHARS_PROPERTY_KEY_METHOD_NAME);
    }
}
