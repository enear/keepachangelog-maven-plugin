package co.enear.maven.plugins.keepachangelog.git;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class GitServerFactoryTest {

    @Test
    public void fromBitBucket() throws MalformedURLException {
        URL url = new URL("https://bitbucket.com/scm/enear/keepachangelog-maven-plugin.git");

        RepoServer actual = GitServerFactory.from(url);

        assertTrue(actual instanceof BitBucketServer);
        //		assertEquals(BitBucketServer.class, actual.getClass());

    }

    @Test
    public void fromGitHub() throws MalformedURLException {
        URL url = new URL("https://github.com/enear/keepachangelog-maven-plugin.git");

        RepoServer actual = GitServerFactory.from(url);

        assertTrue(actual instanceof GitHubServer);
        //		assertEquals(GitHubServer.class, actual.getClass());
    }

    @Test
    public void fromNull() throws MalformedURLException {
        assertNull( GitServerFactory.from( new URL("https://google.com/") ) );
    }

}
