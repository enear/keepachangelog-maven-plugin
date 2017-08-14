package org.enear.changelog.git;

import org.enear.changelog.utils.Range;

import java.net.URL;

public abstract class BaseGitServer implements GitServer {

    @Override
    public URL diff(Range<String> range) {
        String begin = range.getBegin();
        String end = range.getEnd();
        return diff(begin, end);
    }
}
