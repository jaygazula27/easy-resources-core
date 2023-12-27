package com.jgazula.easyresources.core.internal.util;

import com.jgazula.easyresources.core.testutil.TestHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringUtilTests {

    @Test
    void isNullString() {
        var result = StringUtil.isNullOrEmpty(null);
        assertThat(result).isTrue();
    }

    @Test
    void isEmptyString() {
        var result = StringUtil.isNullOrEmpty("");
        assertThat(result).isTrue();
    }

    @Test
    void blankStringIsNotEmpty() {
        var result = StringUtil.isNullOrEmpty(" ");
        assertThat(result).isFalse();
    }

    @Test
    void isNonEmptyString() {
        var result = StringUtil.isNullOrEmpty(TestHelper.randomAlphabetic());
        assertThat(result).isFalse();
    }
}
