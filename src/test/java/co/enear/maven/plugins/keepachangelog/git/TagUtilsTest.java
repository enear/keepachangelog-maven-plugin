package co.enear.maven.plugins.keepachangelog.git;

import co.enear.maven.plugins.keepachangelog.utils.Range;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;

public class TagUtilsTest {

    public static final String UNRELEASED_VERSION = "Unreleased";
    public static final String HEAD = "HEAD";

    @Test
    public void toTagTest() {
        assertEquals("v1.0.0", TagUtils.toTag("v${version}", "1.0.0"));
    }

    @Test
    public void toTagUnreleasedTest() {
        assertEquals(HEAD, TagUtils.toTag("", UNRELEASED_VERSION));
    }

    @Test
    public void toTagRange() {
        Range<String> range = new Range<String>("0.0.0", "3.0.0");

        Range<String> actual = TagUtils.toTagRange("v${version}", range);

        assertEquals("v0.0.0", actual.getBegin());
        assertEquals("v3.0.0", actual.getEnd());

    }

    @Test
    public void toVersionTest() {
        Optional<String> actual = TagUtils.toVersion("v${version}", "v1.0.0");

        assertTrue(actual.isPresent());
        assertEquals("1.0.0", actual.get());
    }



}
