package org.enear.maven.plugins.keepachangelog.markdown.specific;

import org.enear.maven.plugins.keepachangelog.git.RepoServer;
import org.enear.maven.plugins.keepachangelog.git.TagUtils;
import org.enear.maven.plugins.keepachangelog.markdown.Markdown;
import org.enear.maven.plugins.keepachangelog.markdown.generic.RefLink;
import org.enear.maven.plugins.keepachangelog.utils.Range;

import java.net.URL;

/**
 * A changelog version comparison.
 */
public class DiffRefLink implements Markdown {

    private String tagFormat;
    private RepoServer repoServer;
    private Range<String> versionRange;

    /**
     * Creates a new version comparison.
     *
     * @param tagFormat    the tag format.
     * @param gitServer    the git server.
     * @param versionRange the version range.
     */
    public DiffRefLink(String tagFormat, RepoServer repoServer, Range<String> versionRange) {
        this.tagFormat = tagFormat;
        this.repoServer = repoServer;
        this.versionRange = versionRange;
    }

    @Override
    public String toString() {
        return "DiffRefLink(" + versionRange + ")";
    }

    @Override
    public String toMarkdown() {
        String end = versionRange.getEnd();
        Range<String> tagRange = TagUtils.toTagRange(tagFormat, versionRange);
        URL diff = repoServer.diff(tagRange);
        RefLink refLink = new RefLink(end, diff);
        return refLink.toMarkdown();
    }
}
