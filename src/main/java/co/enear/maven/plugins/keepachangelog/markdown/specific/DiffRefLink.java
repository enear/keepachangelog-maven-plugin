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

import co.enear.maven.plugins.keepachangelog.git.RepoServer;
import co.enear.maven.plugins.keepachangelog.markdown.Markdown;
import co.enear.maven.plugins.keepachangelog.markdown.generic.RefLink;
import co.enear.maven.plugins.keepachangelog.utils.Range;
import co.enear.maven.plugins.keepachangelog.git.TagUtils;

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
