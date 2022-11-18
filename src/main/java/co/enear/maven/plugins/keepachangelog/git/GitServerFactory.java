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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * A factory for constructing Git Server representations.
 */
public class GitServerFactory {

    public static final Logger logger = LoggerFactory.getLogger(GitServerFactory.class);

    private static final String BITBUCKET_HOST_PREFIX = "bitbucket";
    private static final String GITHUB_HOST_PREFIX = "github";

    /**
     * Returns a Git server based on a given range URL.
     *
     * @param rangeUrl the range URL
     * @return a repository server
     */
    public static RepoServer from(String rangeUrl, String rangeUrlUnreleased) {
        if (rangeUrl == null) {
            return null;
        } else {
            if (rangeUrlUnreleased == null) {
                logger.info("Using range URL {}", rangeUrl);
                return new CustomGitServer(rangeUrl);
            } else {
                logger.info("Using range URL {} and unreleased range URL {}", rangeUrl, rangeUrlUnreleased);
                return new CustomGitServer(rangeUrl, rangeUrlUnreleased);
            }
        }
    }

    /**
     * Returns a Git Server based on a given repository URL.
     *
     * @param originUrl a repository URL
     * @return a repository server
     * @throws GitServerException if the repository URL is malformed
     */
    public static RepoServer from(URL originUrl) {
        if (originUrl == null) {
            return null;
        }

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
