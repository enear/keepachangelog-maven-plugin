package org.enear.maven.plugins.keepachangelog.git;

import org.enear.maven.plugins.keepachangelog.utils.Range;

import java.net.URL;

public abstract class BaseGitServer implements RepoServer {

    @Override
    public URL diff(Range<String> range) {
        String begin = range.getBegin();
        String end = range.getEnd();
        return diff(begin, end);
    }
}
