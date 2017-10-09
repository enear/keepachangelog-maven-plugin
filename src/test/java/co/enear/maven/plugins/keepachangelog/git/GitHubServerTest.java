package co.enear.maven.plugins.keepachangelog.git;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class GitHubServerTest {

    @Test
    public void diffTest() throws MalformedURLException {

        URL url = new URL("https://github.com/enear/keepachangelog-maven-plugin.git");

        GitHubServer ghs = new GitHubServer(url);

        URL actual = ghs.diff("1.0.0", "3.0.0");
        assertNotNull(actual);
        assertEquals(new URL("https://github.com/enear/keepachangelog-maven-plugin/compare/1.0.0...3.0.0"), actual);
    }


    @Test(expected=GitServerException.class)
    public void diffTestException() throws MalformedURLException {
        URL url = new URL("https://github.com/scm/enear/keepachangelog-maven-plugin.git");

        GitHubServer ghs = new GitHubServer(url);
    }
}
