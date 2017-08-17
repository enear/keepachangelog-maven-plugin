package org.enear.maven.plugins.keepachangelog.markdown.specific;

public class ReaderException extends RuntimeException {

    public ReaderException(String message) {
        super(message);
    }

    public ReaderException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
