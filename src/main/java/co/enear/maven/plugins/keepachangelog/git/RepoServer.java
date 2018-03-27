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

import java.net.URL;

/**
 * Represents a Git Server.
 */
public interface RepoServer {

    /**
     * Returns an URL that compares two commits.
     *
     * @param a the first commit.
     * @param b the second commit.
     * @return an URL that compares two commits.
     * @throws GitServerException if the URL construction fails.
     */
    URL diff(String a, String b);

    /**
     * Returns an URL that compares a range of commits.
     *
     * @param range the range of commits.
     * @return an URL that compares a range of commits.
     * @throws GitServerException if the URL construction fails.
     */
    URL diff(Range<String> range);
}
