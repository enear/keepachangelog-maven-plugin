package org.enear.changelog;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents a Git Server.
 */
public interface GitServer {

    /**
     * Returns an URL that compares two commits.
     *
     * @param a the first commit.
     * @param b the second commit.
     * @return an URL that compares two commits.
     * @throws MalformedURLException if the URL construction fails.
     */
    URL diff(String a, String b) throws MalformedURLException;
}
