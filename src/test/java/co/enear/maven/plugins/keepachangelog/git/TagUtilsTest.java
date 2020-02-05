package co.enear.maven.plugins.keepachangelog.git;

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

import co.enear.maven.plugins.keepachangelog.utils.Range;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;

public class TagUtilsTest {

    public static final String UNRELEASED_VERSION = "Unreleased";
    public static final String HEAD = "HEAD";

    @Test
    public void should_GetEmptyList_WhenUrlIsNull() throws GitAPIException {
        List<String> tags = TagUtils.getTags(null, null, null);
        assertEquals(Collections.emptyList(), tags);
    }

    @Test
    public void should_GetTag_WhenGivenReleasedVersion() {
        assertEquals("v1.0.0", TagUtils.toTag("v${version}", "1.0.0"));
    }

    @Test
    public void should_GetHead_WhenGivenUnreleasedVersion() {
        assertEquals(HEAD, TagUtils.toTag("", UNRELEASED_VERSION));
    }

    @Test
    public void should_GetTagRange_WhenGivenReleasedVersionRange() {
        Range<String> range = new Range<String>("0.0.0", "3.0.0");

        Range<String> actual = TagUtils.toTagRange("v${version}", range);

        assertEquals("v0.0.0", actual.getBegin());
        assertEquals("v3.0.0", actual.getEnd());

    }

    @Test
    public void should_GetReleasedVersion_WhenGivenTag() {
        Optional<String> actual = TagUtils.toVersion("v${version}", "v1.0.0");

        assertTrue(actual.isPresent());
        assertEquals("1.0.0", actual.get());
    }

}
