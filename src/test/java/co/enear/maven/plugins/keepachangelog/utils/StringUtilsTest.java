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

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class StringUtilsTest {

    @Test
    public void should_GetStringOfNChar_WhenNRepetitions() {
        String actual = StringUtils.repeat('c', 4);
        assertEquals("cccc", actual);
    }

    @Test
    public void should_GetEmptyString_WhenZeroRepetitions() {
        String actual = StringUtils.repeat('c', 0);
        assertEquals("", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentException_WhenNegativeRepetitions() {
        StringUtils.repeat('c', -5);
    }

    @Test
    public void should_GetStringWithVariableValue_WhenReplaceVariableInString() {
        String actual = StringUtils.replace("I have ${apples} apples", "apples", "3");
        assertEquals("I have 3 apples", actual);
    }

    @Test
    public void should_GetSameString_WhenReplaceVariableNotInString() {
        String actual = StringUtils.replace("I have ${apples} apples", "oranges", "3");
        assertEquals("I have ${apples} apples", actual);
    }

    @Test
    public void should_GetExtractedValue_WhenExtractStringWithMatchedFormat() {
        Optional<String> actual = StringUtils.extract("v1.0.0", "version", "v${version}");
        assertEquals(Optional.of("1.0.0"), actual);
    }

    @Test
    public void should_GetEmpty_WhenExtractStringWithUnmatchedFormat() {
        Optional<String> actual = StringUtils.extract("3234234", "version", "v${version}");
        assertEquals(Optional.empty(), actual);
    }
}
