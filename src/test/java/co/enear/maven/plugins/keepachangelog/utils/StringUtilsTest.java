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
