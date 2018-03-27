package co.enear.maven.plugins.keepachangelog.markdown.generic;

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

import co.enear.maven.plugins.keepachangelog.markdown.Markdown;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static co.enear.maven.plugins.keepachangelog.utils.StringUtils.repeat;

/**
 * A markdown heading.
 */
public class Heading implements Markdown {

    private static final String MARKERS_ID = "markers";
    private static final String TEXT_ID = "text";
    private static final char OPENING_MARKER = '#';

    private static final String REGEX =
            String.format("^(?<%s>%s+) (?<%s>.*+)$", MARKERS_ID, OPENING_MARKER, TEXT_ID);
    private static final Pattern pattern = Pattern.compile(REGEX);

    private int level;
    private String text;

    /**
     * Creates a new heading.
     *
     * @param level the heading level.
     * @param text  the heading text.
     */
    public Heading(int level, String text) {
        this.level = level;
        this.text = text;
    }

    private String getOpeningMarker() {
        return repeat(OPENING_MARKER, level);
    }

    /**
     * Creates a new heading from a given markdown line.
     *
     * @param line the markdown line.
     * @return a new heading or empty if the markdown line is not a heading.
     */
    public static Optional<Heading> fromMarkdown(String line) {
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

    /**
     * Returns the heading level.
     *
     * @return the heading level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the heading text.
     *
     * @return the heading text.
     */
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Heading(" + level + ", " + text + ")";
    }

    @Override
    public String toMarkdown() {
        return getOpeningMarker() + " " + text;
    }
}
