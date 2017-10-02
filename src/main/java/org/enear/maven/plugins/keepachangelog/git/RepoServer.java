package org.enear.maven.plugins.keepachangelog.git;

import org.enear.maven.plugins.keepachangelog.utils.Range;

import java.net.URL;

/**
 * Represents a Git Server.
 */
public interface RepoServer {

    /**
     * Returns an URL that compares two commits.
     *
     * @param a the first commit.
     * @param b the second commit.
     * @return an URL that compares two commits.
     * @throws GitServerException if the URL construction fails.
     */
    URL diff(String a, String b);

    /**
     * Returns an URL that compares a range of commits.
     *
     * @param range the range of commits.
     * @return an URL that compares a range of commits.
     * @throws GitServerException if the URL construction fails.
     */
    URL diff(Range<String> range);
}
