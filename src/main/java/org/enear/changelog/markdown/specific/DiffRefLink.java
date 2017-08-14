package org.enear.changelog.markdown.specific;

import org.enear.changelog.git.GitServer;
import org.enear.changelog.markdown.generic.Heading;
import org.enear.changelog.markdown.generic.RefLink;
import org.enear.changelog.utils.Range;

import java.net.URL;
import java.util.Optional;

public class DiffRefLink {

    public static final String GIT_HEAD = "HEAD";
    public static final String UNRELEASED_VERSION = "Unreleased";

    public static final String VERSION_ID = "version";
    public static final String VAR_REGEX = "\\$\\{%s\\}";
    public static final String VERSION_REGEX = String.format(VAR_REGEX, VERSION_ID);

    private String tagFormat;
    private GitServer gitServer;
    private Range<String> versionRange;

    public DiffRefLink(String tagFormat, GitServer gitServer, Range<String> versionRange) {
        this.tagFormat = tagFormat;
        this.gitServer = gitServer;
        this.versionRange = versionRange;
    }

    private String toTag(String version) {
        if (version.equals(UNRELEASED_VERSION)) {
            return GIT_HEAD;
        } else {
            return tagFormat.replaceFirst(VERSION_REGEX, version);
        }
    }

    private Range<String> toTagRange() {
        String b = toTag(versionRange.getBegin());
        String e = toTag(versionRange.getEnd());
        return new Range<>(b, e);
    }

    @Override
    public String toString() {
        String begin = versionRange.getBegin();
        URL diff = gitServer.diff(toTagRange());
        RefLink refLink = new RefLink(begin, diff);
        return refLink.toString();
    }
}
