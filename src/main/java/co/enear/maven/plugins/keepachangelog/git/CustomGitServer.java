package co.enear.maven.plugins.keepachangelog.git;

/*-
 * #%L
 * Keep a changelog Maven plugin
 * %%
 * Copyright (C) 2017 - 2020 e.near
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

import co.enear.maven.plugins.keepachangelog.utils.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A custom Git server.
 * <p>
 * The diff URL requires two variables, {@code ${start}} and {@code ${end}}, that will represent a range. For example,
 * a valid GitHub range URL is shown below:
 * <pre>
 * https://github.com/me/myproject/compare/${start}..${end}
 * </pre>
 * This class optionally supports a different URL for the Unreleased link.
 */
public class CustomGitServer extends BaseGitServer implements RepoServer {

    private String rangeUrl;
    private String rangeUrlUnreleased;

    /**
     * Constructs a new custom Git server.
     *
     * @param rangeUrl the range URL with left and right variables
     * @param rangeUrlUnreleased the range URL for Unreleased links with left and right variables
     */
    public CustomGitServer(String rangeUrl, String rangeUrlUnreleased) {
        this.rangeUrl = rangeUrl;
        this.rangeUrlUnreleased = rangeUrlUnreleased;
    }

    /**
     * Constructs a new custom Git server.
     *
     * @param rangeUrl the range URL with left and right variables
     */
    public CustomGitServer(String rangeUrl) {
        this.rangeUrl = rangeUrl;
        this.rangeUrlUnreleased = rangeUrl;
    }

    @Override
    public URL diff(String a, String b) {
        return diff(a, b, rangeUrl);
    }

    @Override
    public URL diffUnreleased(String a, String b) {
        return diff(a, b, rangeUrlUnreleased);
    }

    public URL diff(String a, String b, String customRangeUrl) {
        try {
            String url = customRangeUrl;
            url = StringUtils.replace(url, "start", a);
            url = StringUtils.replace(url, "end", b);
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new GitServerException("Failed to create a diff link.", e);
        }
    }
}
