package org.enear.changelog.markdown.specific;

import org.enear.changelog.markdown.Markdown;
import org.enear.changelog.markdown.generic.Heading;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.enear.changelog.InitMojo.UNRELEASED_VERSION;

/**
 * A changelog version heading.
 */
public class VersionHeading implements Markdown {

    private static final String VERSION_ID = "version";
    private static final String DATE_ID = "date";

    private static final int HEADING_LEVEL = 2;
    private static final String TEXT_REGEX = String.format("^\\[(?<%s>.+)\\](?: - (?<%s>.+))?$", VERSION_ID, DATE_ID);
    private static final Pattern textPattern = Pattern.compile(TEXT_REGEX);

    private String version;
    private LocalDate date;

    /**
     * Creates a new version heading.
     *
     * @param version the version.
     * @param date    the date this version was created.
     */
    public VersionHeading(String version, LocalDate date) {
        this.version = version;
        this.date = date;
    }

    /**
     * Creates a new version heading without a date.
     *
     * @param version the version.
     */
    public VersionHeading(String version) {
        this(version, null);
    }

    /**
     * Creates an unreleased version heading.
     *
     * @return an unreleased version heading.
     */
    public static VersionHeading unreleased() {
        return new VersionHeading(UNRELEASED_VERSION);
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
        Matcher matcher = textPattern.matcher(text);
        if (matcher.matches()) {
            String version = matcher.group(VERSION_ID);
            LocalDate date = parseDate(matcher.group(DATE_ID));
            return Optional.of(new VersionHeading(version, date));
        } else {
            return Optional.empty();
        }
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
     * Returns true if the version is unreleased or false otherwise.
     *
     * @return true if the version is unreleased or false otherwise.
     */
    public boolean isUnreleased() {
        return version.equals(UNRELEASED_VERSION);
    }

    @Override
    public String toString() {
        return "VersionHeading(" + version + ", " + date + ")";
    }

    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();
        sb.append("## [").append(version).append("]");
        if (date != null) {
            sb.append(" - ").append(date);
        }
        return sb.toString();
    }
}
