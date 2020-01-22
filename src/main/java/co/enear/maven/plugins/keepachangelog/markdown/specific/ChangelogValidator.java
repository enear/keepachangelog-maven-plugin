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

import co.enear.maven.plugins.keepachangelog.markdown.generic.RefLink;
import org.eclipse.jgit.api.errors.GitAPIException;
import co.enear.maven.plugins.keepachangelog.git.TagUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static co.enear.maven.plugins.keepachangelog.InitMojo.UNRELEASED_VERSION;

public class ChangelogValidator extends ChangelogReader {

    private Set<String> parsedVersions = new HashSet<>();
    private Set<String> versionsWithoutTags = Collections.emptySet();
    private Set<String> tagsWithoutVersions = Collections.emptySet();

    private String url;
    private String username;
    private String password;
    private String tagFormat;

    public ChangelogValidator(String url, String username, String password, String tagFormat) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.tagFormat = tagFormat;
    }

    @Override
    protected void onVersionHeading(VersionHeading versionHeading) {
        String version = versionHeading.getVersion();
        parsedVersions.add(version);
    }

    @Override
    protected void onRefLink(RefLink refLink) {
    }

    @Override
    protected void onOther(String line) {
    }

    @Override
    public void read(Path path) {
        super.read(path);
        try {
            Set<String> gitVersions = getTagsAsVersions();
            initVersionsWithoutTags(gitVersions);
            initTagsWithoutVersions(gitVersions);
        } catch (GitAPIException e) {
            throw new ReaderException("Failed to get tags as versions.", e);
        }
    }

    private Set<String> getTagsAsVersions() throws GitAPIException {
        List<String> tags = TagUtils.getTags(url, username, password);
        tags.remove(UNRELEASED_VERSION);
        return tags.stream().map(tag ->
                TagUtils.toVersion(tagFormat, tag)).
                filter(Optional::isPresent).
                map(Optional::get).
                collect(Collectors.toSet());
    }

    private void initTagsWithoutVersions(Set<String> gitVersions) {
        tagsWithoutVersions = new HashSet<>(gitVersions);
        tagsWithoutVersions.removeAll(parsedVersions);
    }

    private void initVersionsWithoutTags(Set<String> gitVersions) {
        versionsWithoutTags = new HashSet<>(parsedVersions);
        versionsWithoutTags.remove(UNRELEASED_VERSION);
        versionsWithoutTags.removeAll(gitVersions);
    }

    public Set<String> getVersionsWithoutTags() {
        return versionsWithoutTags;
    }

    public Set<String> getTagsWithoutVersions() {
        return tagsWithoutVersions;
    }
}
