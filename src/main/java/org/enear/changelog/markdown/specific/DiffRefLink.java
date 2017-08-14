package org.enear.changelog.markdown.specific;

import org.enear.changelog.git.GitServer;
import org.enear.changelog.git.TagUtils;
import org.enear.changelog.markdown.Markdown;
import org.enear.changelog.markdown.generic.RefLink;
import org.enear.changelog.utils.Range;

import java.net.URL;

/**
 * A changelog version comparison.
 */
public class DiffRefLink implements Markdown {

    private String tagFormat;
    private GitServer gitServer;
    private Range<String> versionRange;

    /**
     * Creates a new version comparison.
     *
     * @param tagFormat    the tag format.
     * @param gitServer    the git server.
     * @param versionRange the version range.
     */
    public DiffRefLink(String tagFormat, GitServer gitServer, Range<String> versionRange) {
        this.tagFormat = tagFormat;
        this.gitServer = gitServer;
        this.versionRange = versionRange;
    }

    @Override
    public String toString() {
        return "DiffRefLink(" + versionRange + ")";
    }

    @Override
    public String toMarkdown() {
        String begin = versionRange.getBegin();
        Range<String> tagRange = TagUtils.toTagRange(tagFormat, versionRange);
        URL diff = gitServer.diff(tagRange);
        RefLink refLink = new RefLink(begin, diff);
        return refLink.toMarkdown();
    }
}
