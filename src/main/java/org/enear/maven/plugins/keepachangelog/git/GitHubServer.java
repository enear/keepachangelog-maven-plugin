package org.enear.maven.plugins.keepachangelog.git;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHubServer extends BaseGitServer implements GitServer {

    private static final String USER_ID = "username";
    private static final String REPO_ID = "repository";
    private static final String PATH_REGEX =
            String.format("/(?<%s>[^/]*)/(?<%s>[^/]*)\\.git", USER_ID, REPO_ID);

    public static final Pattern pathPattern = Pattern.compile(PATH_REGEX);

    private URL originUrl;
    private URL diffUrl;
    private String username;
    private String repository;

    /**
     * Creates a new GitHub representation.
     *
     * @param originUrl the Git repository URL.
     * @throws GitServerException if the given URL is not in the GitHub format.
     */
    public GitHubServer(URL originUrl) {
        this.originUrl = originUrl;
        String path = this.originUrl.getPath();
        Matcher matcher = pathPattern.matcher(path);
        if (matcher.matches()) {
            this.username = matcher.group(USER_ID);
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

    private String getUsername() {
        return username;
    }

    private String getRepository() {
        return repository;
    }

    private void initDiffUrl() throws MalformedURLException {
        if (diffUrl == null) {
            String protocol = getProtocol();
            String host = getHost();
            String username = getUsername();
            String repo = getRepository();
            String spec = String.format("%s://%s/%s/%s/compare",
                    protocol, host, username, repo);
            this.diffUrl = new URL(spec);
        }
    }

    @Override
    public URL diff(String a, String b) {
        try {
            initDiffUrl();
            String spec = String.format("%s/%s..%s", diffUrl, a, b);
            return new URL(spec);
        } catch (MalformedURLException e) {
            throw new GitServerException("Failed to create a diff link.", e);
        }
    }
}
