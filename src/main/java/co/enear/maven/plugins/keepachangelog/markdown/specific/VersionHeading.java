package co.enear.maven.plugins.keepachangelog.markdown.specific;

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

import co.enear.maven.plugins.keepachangelog.InitMojo;
import co.enear.maven.plugins.keepachangelog.markdown.Markdown;
import co.enear.maven.plugins.keepachangelog.markdown.generic.Heading;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A changelog version heading.
 */
public class VersionHeading implements Markdown {

    private static final String VERSION_ID = "version";
    private static final String DATE_ID = "date";

    private static final int HEADING_LEVEL = 2;
    private static final String VERSION_REGEX = "(?<" + VERSION_ID + ">[\\w\\.-]+)";
    private static final String VERSION_LINK_REGEX = "\\[" + VERSION_REGEX + "\\]";
    private static final String DATE_REGEX = "(?: - (?<" + DATE_ID + ">.+))?";
    private static final String TEXT_REGEX = "^" + VERSION_REGEX + DATE_REGEX + "$";
    private static final String TEXT_LINK_REGEX = "^" + VERSION_LINK_REGEX + DATE_REGEX + "$";

    private static final Pattern textPattern = Pattern.compile(TEXT_REGEX);
    private static final Pattern textLinkPattern = Pattern.compile(TEXT_LINK_REGEX);

    private String version;
    private LocalDate date;
    private boolean refLink;

    /**
     * Creates a new version heading.
     *
     * @param version the version.
     * @param date    the date this version was created.
     * @param refLink true if the version is a reference link or false otherwise.
     */
    public VersionHeading(String version, LocalDate date, boolean refLink) {
        this.version = version;
        this.date = date;
        this.refLink = refLink;
    }

    /**
     * Creates a new version heading without a date.
     *
     * @param version the version.
     * @param refLink true the version is a reference link or false otherwise.
     */
    public VersionHeading(String version, boolean refLink) {
        this(version, null, refLink);
    }

    /**
     * Creates an unreleased version heading.
     *
     * @param refLink true the version is a reference link or false otherwise.
     * @return an unreleased version heading.
     */
    public static VersionHeading unreleased(boolean refLink) {
        return new VersionHeading(InitMojo.UNRELEASED_VERSION, refLink);
    }

    private static Optional<VersionHeading> parse(String text, Pattern pattern, boolean refLink) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String version = matcher.group(VERSION_ID);
            LocalDate date = parseDate(matcher.group(DATE_ID));
            return Optional.of(new VersionHeading(version, date, refLink));
        } else {
            return Optional.empty();
        }
    }

    private static Optional<VersionHeading> parseNoLink(String text) {
        return parse(text, textPattern, false);
    }

    private static Optional<VersionHeading> parseLink(String text) {
        return parse(text, textLinkPattern, true);
    }

    /**
     * Creates a version heading from a heading.
     *
     * @param heading the heading.
     * @return a version heading or empty if the heading is not a version heading.
     */
    public static Optional<VersionHeading> parse(Heading heading) {
        if (heading.getLevel() != HEADING_LEVEL)
            return Optional.empty();

        String text = heading.getText();
        Optional<VersionHeading> opt = parseLink(text);
        if (!opt.isPresent())
            opt = parseNoLink(text);
        return opt;
    }

    /**
     * Creates a version heading from a markdown line.
     *
     * @param line the markdown line.
     * @return a version heading or empty if the markdown line is not a version heading.
     */
    public static Optional<VersionHeading> parse(String line) {
        return Heading.fromMarkdown(line).flatMap(heading ->
                parse(heading)
        );
    }

    private static LocalDate parseDate(String date) {
        if (date != null)
            return LocalDate.parse(date);
        else
            return null;
    }

    /**
     * Returns the version.
     *
     * @return the version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Returns the creation date of the version.
     *
     * @return the creation date of the version.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns true if the version is a reference link or false otherwise.
     *
     * @return true if the version is a reference link or false otherwise.
     */
    public boolean isRefLink() {
        return refLink;
    }

    /**
     * Returns true if the version is unreleased or false otherwise.
     *
     * @return true if the version is unreleased or false otherwise.
     */
    public boolean isUnreleased() {
        return version.equals(InitMojo.UNRELEASED_VERSION);
    }

    @Override
    public String toString() {
        return "VersionHeading(" + version + ", " + date + ", " + refLink + ")";
    }

    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();
        sb.append("## ");
        if (refLink)
            sb.append("[");
        sb.append(version);
        if (refLink)
            sb.append("]");
        if (date != null) {
            sb.append(" - ").append(date);
        }
        return sb.toString();
    }
}
