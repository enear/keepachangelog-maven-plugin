package org.enear.maven.plugins.keepachangelog.markdown.specific;

import org.enear.maven.plugins.keepachangelog.InitMojo;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.*;

public class VersionHeadingTest {

    private String expectedUnreleasedMarkdown(boolean refLink) {
        if (refLink) {
            return "[" + InitMojo.UNRELEASED_VERSION + "]";
        } else {
            return InitMojo.UNRELEASED_VERSION;
        }
    }

    private void assertUnreleased(VersionHeading version, boolean refLink) {
        assertEquals(InitMojo.UNRELEASED_VERSION, version.getVersion());
        assertEquals(null, version.getDate());
        assertEquals(refLink, version.isRefLink());
        assertEquals("## " + expectedUnreleasedMarkdown(refLink), version.toMarkdown());
        assertTrue(version.isUnreleased());
        assertEquals("VersionHeading(" + InitMojo.UNRELEASED_VERSION + ", null, " + refLink + ")",
                version.toString());
    }

    @Test
    public void parseNoLink() {
        Optional<VersionHeading> opt = VersionHeading.parse("## 1.0.0 - 2017-08-03");
        assertTrue(opt.isPresent());

        VersionHeading version = opt.get();
        assertEquals("1.0.0", version.getVersion());
        assertEquals(LocalDate.parse("2017-08-03"), version.getDate());
        assertEquals(false, version.isRefLink());
        assertEquals("## 1.0.0 - 2017-08-03", version.toMarkdown());
        assertFalse(version.isUnreleased());
        assertEquals("VersionHeading(1.0.0, 2017-08-03, false)", version.toString());
    }

    @Test
    public void parseLink() throws Exception {
        Optional<VersionHeading> opt = VersionHeading.parse("## [1.5.0] - 2018-07-03");
        assertTrue(opt.isPresent());

        VersionHeading version = opt.get();
        assertEquals("1.5.0", version.getVersion());
        assertEquals(LocalDate.parse("2018-07-03"), version.getDate());
        assertEquals(true, version.isRefLink());
        assertEquals("## [1.5.0] - 2018-07-03", version.toMarkdown());
        assertFalse(version.isUnreleased());
        assertEquals("VersionHeading(1.5.0, 2018-07-03, true)", version.toString());
    }

    @Test
    public void parseUnreleasedLink() {
        Optional<VersionHeading> opt = VersionHeading.parse("## [Unreleased]");
        assertTrue(opt.isPresent());

        VersionHeading version = opt.get();
        assertUnreleased(version, true);
    }

    @Test
    public void parseUnreleasedNoLink() {
        Optional<VersionHeading> opt = VersionHeading.parse("## Unreleased");
        assertTrue(opt.isPresent());

        VersionHeading version = opt.get();
        assertUnreleased(version, false);
    }

    @Test
    public void parseWrongHeading() {
        Optional<VersionHeading> opt = VersionHeading.parse("### Unreleased");
        assertFalse(opt.isPresent());
    }

    @Test
    public void testUnreleased() {
        VersionHeading version = VersionHeading.unreleased(false);
        assertUnreleased(version, false);
    }
}