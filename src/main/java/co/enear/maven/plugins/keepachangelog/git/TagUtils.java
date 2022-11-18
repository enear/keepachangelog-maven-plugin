package co.enear.maven.plugins.keepachangelog.git;

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

import co.enear.maven.plugins.keepachangelog.utils.Range;
import co.enear.maven.plugins.keepachangelog.utils.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static co.enear.maven.plugins.keepachangelog.InitMojo.UNRELEASED_VERSION;

/**
 * Git utilities.
 */
public class TagUtils {

    private static final String VERSION_ID = "version";
    public static final String DEFAULT_TAG_FORMAT = "v${" + VERSION_ID + "}";

    public static final String DEFAULT_UNRELEASED_GITREF = "HEAD";
    private static final String REFS_TAGS = "refs/tags/";

    private static String getTagName(Ref tagRef) {
        return tagRef.getName().replaceFirst(REFS_TAGS, StringUtils.EMPTY_STRING);
    }

    /**
     * Returns the tags of the given Git repository.
     *
     * @param url      the Git repository URL.
     * @param username the username to connect to the URL.
     * @param password the password to connect to the URL.
     * @return a list of tags.
     * @throws GitAPIException if an error occurs while calling a Git command
     */
    public static List<String> getTags(String url, String username, String password)
            throws GitAPIException {
        if (url == null)
            return Collections.emptyList();

        LsRemoteCommand cmd = Git.lsRemoteRepository();
        cmd.setRemote(url);
        if (username != null && password != null) {
            cmd.setCredentialsProvider(
                    new UsernamePasswordCredentialsProvider(username, password)
            );
        }
        Collection<Ref> refs = cmd.call();
        return refs.stream()
                .filter(ref -> ref.getName().startsWith(REFS_TAGS))
                .map(TagUtils::getTagName)
                .collect(Collectors.toList());
    }

    /**
     * Converts a version to a tag.
     *
     * @param tagFormat the format of the tag (e.g., {@code v${version}}.
     * @param version   the version to be converted.
     * @return a tag.
     */
    public static String toTag(String tagFormat, String version, String unreleasedGitRef) {
        if (version.equals(UNRELEASED_VERSION)) {
            return unreleasedGitRef;
        } else {
            return StringUtils.replace(tagFormat, VERSION_ID, version);
        }
    }

    /**
     * Converts a version range to a tag range.
     *
     * @param tagFormat    the tag format.
     * @param versionRange the version range.
     * @return a tag range.
     */
    public static Range<String> toTagRange(String tagFormat, Range<String> versionRange, String unreleasedGitRef) {
        String b = toTag(tagFormat, versionRange.getBegin(), unreleasedGitRef);
        String e = toTag(tagFormat, versionRange.getEnd(), unreleasedGitRef);
        return new Range<>(b, e);
    }

    /**
     * Converts a tag to a version.
     *
     * @param tagFormat the format of the tag (e.g., {@code v${version}}.
     * @param tag       the tag to be converted.
     * @return the version.
     */
    public static Optional<String> toVersion(String tagFormat, String tag) {
        return StringUtils.extract(tag, VERSION_ID, tagFormat);
    }
}
