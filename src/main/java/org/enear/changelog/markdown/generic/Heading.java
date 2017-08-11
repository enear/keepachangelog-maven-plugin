package org.enear.changelog.markdown.generic;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Heading {

    private static final String MARKERS_ID = "markers";
    private static final String TEXT_ID = "text";
    private static final char OPENING_MARKER = '#';

    private static final String REGEX =
            String.format("^(?<%s>%s+) (?<%s>.*+)$", MARKERS_ID, OPENING_MARKER, TEXT_ID);
    private static final Pattern pattern = Pattern.compile(REGEX);

    private int level;
    private String text;

    public Heading(int level, String text) {
        this.level = level;
        this.text = text;
    }

    private String repeat(char c, int n) {
        char[] cs = new char[n];
        Arrays.fill(cs, c);
        return new String(cs);
    }

    private String getOpeningMarker() {
        return repeat(OPENING_MARKER, level);
    }

    public static Optional<Heading> from(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            String markers = matcher.group(MARKERS_ID);
            int level = markers.length();
            String text = matcher.group(TEXT_ID);
            return Optional.of(new Heading(level, text));
        } else {
            return Optional.empty();
        }
    }

    public int getLevel() {
        return level;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return getOpeningMarker() + " " + text;
    }
}
