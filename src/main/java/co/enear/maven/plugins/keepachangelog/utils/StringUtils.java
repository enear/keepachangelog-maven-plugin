package co.enear.maven.plugins.keepachangelog.utils;

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

    public static Optional<String> extract(String str, String id, String format) {
        String regex = replace(format, id, "(?<" + id + ">.*)");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return Optional.of(matcher.group(id));
        } else {
            return Optional.empty();
        }
    }

    public static void main(String[] args) {
        Optional<String> version = StringUtils.extract("v1.0.0", "version", "v${version}");
        System.out.println(version);
    }
}
