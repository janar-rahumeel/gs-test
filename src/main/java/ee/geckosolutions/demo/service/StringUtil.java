package ee.geckosolutions.demo.service;

import org.springframework.util.StringUtils;

public final class StringUtil {

    private StringUtil() {
    }

    public static Integer resolveWhiteSpaceIndent(String value) {
        int occurrences = StringUtils.countOccurrencesOf(value, "\u00a0");
        if (occurrences > 0) {
            occurrences = occurrences / 4;
        }
        return occurrences;
    }

}
