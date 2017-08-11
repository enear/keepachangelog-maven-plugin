package org.enear.changelog.markdown.generic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RefLink {

    private static final String REF_ID = "ref";
    private static final String LINK_ID = "link";

    private static final String REGEX = String.format("^\\[(?<%s>.+)\\]: (?<%s>.+)$", REF_ID, LINK_ID);
    private static final Pattern pattern = Pattern.compile(REGEX);

    private String ref;
    private URL link;

    public RefLink(String ref, URL link) throws MalformedURLException {
        this.ref = ref;
        this.link = link;
    }

    public static Optional<RefLink> parse(String line) throws MalformedURLException {
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            String ref = matcher.group(REF_ID);
            URL link = new URL(matcher.group(LINK_ID));
            return Optional.of(new RefLink(ref, link));
        } else {
            return Optional.empty();
        }
    }

    public String toString() {
        return String.format("[%s]: %s", ref, link);
    }

}
