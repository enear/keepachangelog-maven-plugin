package co.enear.maven.plugins.keepachangelog.markdown.specific;

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
        if (!parsedLine) {
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
                stream.forEach(line -> readLine(line));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new ReaderException("Failed to read file.", e);
        }
    }
}
