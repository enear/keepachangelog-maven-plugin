package org.enear.maven.plugins.keepachangelog.git;

import java.net.URL;

/**
 * A factory for constructing Git Server representations.
 */
public class GitServerFactory {

    private static final String BITBUCKET_HOST_PREFIX = "bitbucket";
    private static final String GITHUB_HOST_PREFIX = "github";

    /**
     * Returns a Git Server based on a given repository URL.
     *
     * @param originUrl a repository URL.
     * @return a Repository Server.
     * @throws GitServerException if the repository URL is malformed.
     */
    public static RepoServer from(URL originUrl) {
        String host = originUrl.getHost();
        if (host.startsWith(BITBUCKET_HOST_PREFIX)) {
            return new BitBucketServer(originUrl);
        } else if (host.startsWith(GITHUB_HOST_PREFIX)) {
            return new GitHubServer(originUrl);
        } else {
            return null;
        }
    }

}
