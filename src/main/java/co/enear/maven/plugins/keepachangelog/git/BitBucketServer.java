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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A BitBucket server representation.
 */
public class BitBucketServer extends BaseGitServer implements RepoServer {

    private static final String PROJECT_ID = "project";
    private static final String REPO_ID = "repository";
    private static final String PATH_REGEX =
            String.format("/scm/(?<%s>[^/]+)/(?<%s>[^/]+)\\.git", PROJECT_ID, REPO_ID);

    public static final Pattern pathPattern = Pattern.compile(PATH_REGEX);

    private URL originUrl;
    private URL diffUrl;
    private String project;
    private String repository;

    /**
     * Creates a new BitBucketServer representation.
     *
     * @param originUrl the Git repository URL.
     * @throws GitServerException if the given URL is not in the BitBucket format.
     */
    public BitBucketServer(URL originUrl) {
        this.originUrl = originUrl;
        String path = this.originUrl.getPath();
        Matcher matcher = pathPattern.matcher(path);
        if (matcher.matches()) {
            this.project = matcher.group(PROJECT_ID);
            this.repository = matcher.group(REPO_ID);
        } else {
            throw new GitServerException("Unknown path format.");
        }
    }

    private String getProtocol() {
        return originUrl.getProtocol();
    }

    private String getHost() {
        return originUrl.getHost();
    }

    private String getProject() {
        return project;
    }

    private String getRepository() {
        return repository;
    }

    private void initDiffUrl() throws MalformedURLException {
        if (diffUrl == null) {
            String protocol = getProtocol();
            String host = getHost();
            String project = getProject();
            String repo = getRepository();
            String spec = String.format("%s://%s/projects/%s/repos/%s/branches/compare",
                    protocol, host, project, repo);
            this.diffUrl = new URL(spec);
        }
    }

    @Override
    public URL diff(String a, String b) {
        try {
            initDiffUrl();
            String spec = String.format("%s/%s%%0D%s", diffUrl, a, b);
            return new URL(spec);
        } catch (MalformedURLException e) {
            throw new GitServerException("Failed to create a diff link.", e);
        }
    }
}
