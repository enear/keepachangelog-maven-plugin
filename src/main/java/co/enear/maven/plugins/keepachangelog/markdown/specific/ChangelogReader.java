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

import co.enear.maven.plugins.keepachangelog.markdown.generic.RefLink;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class ChangelogReader {

    protected abstract void onVersionHeading(VersionHeading versionHeading);

    protected abstract void onRefLink(RefLink refLink);

    protected abstract void onOther(String line);

    private void readLine(String line) {
        boolean parsedLine = false;
        {
            Optional<VersionHeading> opt = VersionHeading.parse(line);
            if (opt.isPresent()) {
                VersionHeading versionHeading = opt.get();
                onVersionHeading(versionHeading);
                parsedLine = true;
            }
        }

        if (!parsedLine) {
            Optional<RefLink> opt = RefLink.fromMarkdown(line);
            if (opt.isPresent()) {
                RefLink refLink = opt.get();
                onRefLink(refLink);
                parsedLine = true;
            }
        }

        if (!parsedLine) {
            onOther(line);
        }
    }

    public void read(Path path) {
        try {
            try (Stream<String> stream = Files.lines(path)) {
                stream.forEach(this::readLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new ReaderException("Failed to read file.", e);
        }
    }
}
