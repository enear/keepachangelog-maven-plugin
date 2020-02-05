package co.enear.maven.plugins.keepachangelog.utils;

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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RangeTest {

    @Test
    public void should_GetLeftArg_WhenGetBegin() {
        assertEquals("1", new Range<>("1", "12").getBegin());
    }

    @Test
    public void should_GetRightArg_WhenGetEnd() {
        assertEquals("12", new Range<>("1", "12").getEnd());
    }

    @Test
    public void should_CreateSameIndexPairs_ZipSameSizeLists() {
        List<Range<Integer>> actual = Range.zip(Arrays.asList(1, 2), Arrays.asList(3, 4));
        List<Range<Integer>> expected = new ArrayList<>();
        expected.add(new Range<>(1, 3));
        expected.add(new Range<>(2, 4));
        assertEquals(expected, actual);
    }

    @Test
    public void should_DropExtraElements_ZipDifferentSizeLists() {
        List<Range<Integer>> actual;
        List<Range<Integer>> expected;

        actual = Range.zip(Arrays.asList(1, 2), Arrays.asList(4, 5, 6));
        expected = new ArrayList<>();
        expected.add(new Range<>(1, 4));
        expected.add(new Range<>(2, 5));
        assertEquals(expected, actual);

        actual = Range.zip(Arrays.asList(1, 2, 3), Arrays.asList(4, 5));
        expected = new ArrayList<>();
        expected.add(new Range<>(1, 4));
        expected.add(new Range<>(2, 5));
        assertEquals(expected, actual);

        actual = Range.zip(Arrays.asList(1, 2, 3), Collections.emptyList());
        expected = new ArrayList<>();
        assertEquals(expected, actual);
    }

}
