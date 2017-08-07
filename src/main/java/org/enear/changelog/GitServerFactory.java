package org.enear.changelog;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A factory for constructing Git Server representations.
 */
public class GitServerFactory {

    private static final String BITBUCKET_HOST_PREFIX = "bitbucket";

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
        } else {
            throw new UnknownGitServerException();
        }
    }

}
