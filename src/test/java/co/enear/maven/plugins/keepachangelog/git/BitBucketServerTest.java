package co.enear.maven.plugins.keepachangelog.git;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class BitBucketServerTest {

    @Test
    public void diffTest() throws MalformedURLException {

        URL url = new URL("https://bitbucket.com/scm/enear/keepachangelog-maven-plugin.git");

        BitBucketServer bbs = new BitBucketServer(url);

        URL actual = bbs.diff("1.0.0", "3.0.0");
        assertNotNull(actual);
        assertEquals(new URL("https://bitbucket.com/projects/enear/repos/keepachangelog-maven-plugin/branches/compare/1.0.0%0D3.0.0"), actual);
    }

    @Test(expected=GitServerException.class)
    public void diffTestException() throws MalformedURLException {
        URL url = new URL("https://bitbucket.com/enear/keepachangelog-maven-plugin.git");

        BitBucketServer bbs = new BitBucketServer(url);
    }
}
