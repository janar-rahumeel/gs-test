package ee.geckosolutions.demo.service;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StringUtilTest {

    @Test
    public void testThatWhenFileDoesNotExistExceptionIsTriggered() {
        // given
        String text = "\u00a0\u00a0\u00a0\u00a0Text";

        // when
        int indentCount = StringUtil.resolveWhiteSpaceIndent(text);

        // then
        assertThat(indentCount, is(equalTo(1)));
    }

}
