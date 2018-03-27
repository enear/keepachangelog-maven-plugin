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

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RangeTest {

    private Range range;

    @Before
    public void setup() {
        range = new Range<String>("1", "12");
    }

    @Test
    public void getBeginTest() {
        assertEquals("1", range.getBegin());
    }

    @Test
    public void getEndTest() {
        assertEquals("12", range.getEnd());
    }

    @Test
    public void zipTest() {

        List<Range<String>> actual = Range.zip(Arrays.asList("1", "2", "3"), Arrays.asList("4", "5", "6", "7", "8"));

        assertEquals(3, actual.size());
        assertEquals("1", actual.get(0).getBegin());
        assertEquals("4", actual.get(0).getEnd());

        assertEquals("2", actual.get(1).getBegin());
        assertEquals("5", actual.get(1).getEnd());

        assertEquals("3", actual.get(2).getBegin());
        assertEquals("6", actual.get(2).getEnd());

    }

}
