package org.enear.changelog;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
     * @return a Git Server.
     * @throws MalformedURLException if the repository URL is malformed.
     */
    public static GitServer from(URL originUrl) throws MalformedURLException {
        String host = originUrl.getHost();
        if (host.startsWith(BITBUCKET_HOST_PREFIX)) {
            return new BitBucketServer(originUrl);
        } else if (host.startsWith(GITHUB_HOST_PREFIX)) {
            return new GitHubServer(originUrl);
        } else {
            throw new UnknownGitServerException();
        }
    }

}
