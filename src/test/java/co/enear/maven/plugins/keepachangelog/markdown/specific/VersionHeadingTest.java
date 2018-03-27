package co.enear.maven.plugins.keepachangelog.markdown.specific;

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

import co.enear.maven.plugins.keepachangelog.InitMojo;
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
