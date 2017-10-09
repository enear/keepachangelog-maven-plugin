package co.enear.maven.plugins.keepachangelog.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Optional;

import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void repeatTest() {
        assertEquals("cccc", StringUtils.repeat('c', 4));

    }

    @Test
    public void replaceTest() {
        assertEquals("I have 3 apples", StringUtils.replace("I have ${apples} apples", "apples", "3")); 

    }

    @Test
    public void extractTest() {
        Optional<String> actual = StringUtils.extract("v1.0.0", "version", "v${version}");

        assertEquals("1.0.0", actual.get());
    }

    @Test
    public void extractEmptyTest() {
        Optional<String> actual = StringUtils.extract("3234234", "version", "v${version}");

        assertFalse(actual.isPresent());
    }
}
