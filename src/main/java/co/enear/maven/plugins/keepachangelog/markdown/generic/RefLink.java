package co.enear.maven.plugins.keepachangelog.markdown.generic;

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

import co.enear.maven.plugins.keepachangelog.markdown.Markdown;
import co.enear.maven.plugins.keepachangelog.markdown.ParseException;

import java.net.URL;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A markdown reference link.
 */
public class RefLink implements Markdown {

    private static final String REF_ID = "ref";
    private static final String LINK_ID = "link";

    private static final String REGEX = String.format("^\\[(?<%s>.+)\\]: (?<%s>.+)$", REF_ID, LINK_ID);
    private static final Pattern pattern = Pattern.compile(REGEX);

    private String ref;
    private URL link;

    /**
     * Creates a new reference link.
     *
     * @param ref  the reference.
     * @param link the link.
     */
    public RefLink(String ref, URL link) {
        this.ref = ref;
        this.link = link;
    }

    public String getRef() {
        return ref;
    }

    public URL getLink() {
        return link;
    }

    /**
     * Creates a new reference link from a given markdown line.
     *
     * @param line the markdown line.
     * @return a new reference link or empty if the markdown line is not a reference link.
     */
    public static Optional<RefLink> fromMarkdown(String line) {
        try {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String ref = matcher.group(REF_ID);
                URL link = new URL(matcher.group(LINK_ID));
                return Optional.of(new RefLink(ref, link));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new ParseException("Failed to fromMarkdown reference link.", e);
        }
    }

    @Override
    public String toString() {
        return "RefLink(" + ref + ", " + link + ")";
    }


    @Override
    public String toMarkdown() {
        return String.format("[%s]: %s", ref, link);
    }
}
