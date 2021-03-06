package co.enear.maven.plugins.keepachangelog.utils;

/*-
 * #%L
 * keepachangelog-maven-plugin
 * %%
 * Copyright (C) 2017 - 2018 e.near
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String utilities.
 */
public class StringUtils {

    public static final String EMPTY_STRING = "";
    public static final String VAR_REGEX = "\\$\\{%s\\}";

    /**
     * Repeats the given characters {@code n} times.
     *
     * @param c the character to be repeated.
     * @param n the number of repetitions.
     * @return the given character {@code n} times.
     */
    public static String repeat(char c, int n) {
        if (n < 0)
            throw new IllegalArgumentException("The number of repetition cannot be negative");
        char[] cs = new char[n];
        Arrays.fill(cs, c);
        return new String(cs);
    }

    /**
     * Replaces a variable in a string.
     * <p>
     * For example;
     * <pre>{@code
     * replace("I have ${apples} apples", "apples", 3);
     * }
     * </pre>
     * <p>
     * Will return:
     * <pre>{@code
     * I have 3 apples
     * }
     * </pre>
     *
     * @param str   the string.
     * @param id    the id of the variable.
     * @param value the value.
     * @return the string with the value.
     */
    public static String replace(String str, String id, String value) {
        String regex = String.format(VAR_REGEX, id);
        return str.replaceAll(regex, value);
    }

    public static Optional<String> extract(String str, String id, String format) {
        String regex = replace(format, id, "(?<" + id + ">.*)");
        Pattern pattern = Pattern.compile("^" + regex + "$");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return Optional.of(matcher.group(id));
        } else {
            return Optional.empty();
        }
    }

}
