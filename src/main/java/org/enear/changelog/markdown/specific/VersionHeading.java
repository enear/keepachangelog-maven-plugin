package org.enear.changelog.markdown.specific;

import jdk.nashorn.internal.parser.DateParser;
import org.enear.changelog.markdown.generic.Heading;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionHeading {

    public static final String UNRELEASED_VERSION = "Unreleased";

    private static final int HEADING_LEVEL = 2;
    private static final String VERSION_ID = "version";
    private static final String DATE_ID = "date";

    private static final String TEXT_REGEX = String.format("^\\[(?<%s>.+)\\](?: - (?<%s>.+))?$", VERSION_ID, DATE_ID);

    private static final Pattern textPattern = Pattern.compile(TEXT_REGEX);

    private String version;
    private LocalDate date;

    public VersionHeading(String version, LocalDate date) {
        this.version = version;
        this.date = date;
    }

    public VersionHeading(String version) {
        this(version, null);
    }

    public static VersionHeading unreleased() {
        return new VersionHeading(UNRELEASED_VERSION);
    }

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

    public static Optional<VersionHeading> parse(String line) {
        return Heading.from(line).flatMap(heading ->
                parse(heading)
        );
    }

    private static LocalDate parseDate(String date) {
        if (date != null)
            return LocalDate.parse(date);
        else
            return null;
    }

    public String getVersion() {
        return version;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isUnreleased() {
        return version.equals(UNRELEASED_VERSION);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("## [").append(version).append("]");
        if (date != null) {
            sb.append(" - ").append(date);
        }
        return sb.toString();
    }
}
