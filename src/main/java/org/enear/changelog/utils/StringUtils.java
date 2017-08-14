package org.enear.changelog.utils;

import java.util.Arrays;

/**
 * String utilities.
 */
public class StringUtils {

    public static final String EMPTY_STRING = "";
    public static final String VAR_REGEX = "\\$\\{%s\\}";

    /**
     * Repeats the given characters {@code n} times.
     *
     * @param c the
     * @param n
     * @return
     */
    public static String repeat(char c, int n) {
        char[] cs = new char[n];
        Arrays.fill(cs, c);
        return new String(cs);
    }

    /**
     * Replaces embed variable reference with a value.
     * <p>
     * For example
     * <p>
     * <pre>{@code
     * replace("I have ${apples} apples", "apples", 3);
     * }
     * </pre>
     * <p>
     * Will return:
     * <p>
     * <pre>{@code
     * I have 3 apples
     * }
     * </pre>
     *
     * @param str
     * @param id
     * @param value
     * @return
     */
    public static String replace(String str, String id, String value) {
        String regex = String.format(VAR_REGEX, id);
        return str.replaceAll(regex, value);
    }
}
