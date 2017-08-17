package org.enear.maven.plugins.keepachangelog.markdown;

/**
 * A runtime exception to be thrown when a fromMarkdown error occurs.
 */
public class ParseException extends RuntimeException {

    /**
     * Creates a fromMarkdown exception.
     *
     * @param message the fromMarkdown exception message.
     */
    public ParseException(String message) {
        super(message);
    }

    /**
     * Create a fromMarkdown exception.
     *
     * @param message the fromMarkdown exception message.
     * @param cause   the cause of the exception.
     */
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
