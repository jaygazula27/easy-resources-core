package com.jgazula.typesaferesources.core.internal.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class PropertiesParserTests {

    private static final String ONE_WORD_PROPERTY_KEY = "key";
    private static final String ONE_WORD_PROPERTY_KEY_VARIABLE = "KEY";
    private static final String TWO_WORDS_PROPERTY_KEY = "my.key";
    private static final String TWO_WORDS_PROPERTY_KEY_VARIABLE = "MY_KEY";
    private static final String OTHER_CHARS_PROPERTY_KEY = "my-key@with_other.chars";
    private static final String OTHER_CHARS_PROPERTY_KEY_VARIABLE = "MY_KEY_WITH_OTHER_CHARS";

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
        assertThat(parser.keyToStaticFinalVariable(ONE_WORD_PROPERTY_KEY)).isEqualTo(ONE_WORD_PROPERTY_KEY_VARIABLE);
    }

    @Test
    void parseMultipleWordsAsStaticFinalVariable() {
        assertThat(parser.keyToStaticFinalVariable(TWO_WORDS_PROPERTY_KEY)).isEqualTo(TWO_WORDS_PROPERTY_KEY_VARIABLE);
    }

    @Test
    void parseKeyWithHyphensAndOtherCharsAsStaticFinalVariable() {
        assertThat(parser.keyToStaticFinalVariable(OTHER_CHARS_PROPERTY_KEY))
                .isEqualTo(OTHER_CHARS_PROPERTY_KEY_VARIABLE);
    }
}
