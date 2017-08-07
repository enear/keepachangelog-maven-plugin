package org.enear.changelog;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A BitBucket server representation.
 */
public class BitBucketServer implements GitServer {

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
     * @throws MalformedURLException
     */
    public BitBucketServer(URL originUrl) throws MalformedURLException {
        this.originUrl = originUrl;
        String path = this.originUrl.getPath();
        Matcher matcher = pathPattern.matcher(path);
        if (matcher.matches()) {
            this.project = matcher.group(PROJECT_ID);
            this.repository = matcher.group(REPO_ID);
        } else {
            throw new MalformedURLException("Unknown path format.");
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
    public URL diff(String a, String b) throws MalformedURLException {
        initDiffUrl();
        String spec = String.format("%s/%s%%0D%s", diffUrl, a, b);
        return new URL(spec);
    }
}
