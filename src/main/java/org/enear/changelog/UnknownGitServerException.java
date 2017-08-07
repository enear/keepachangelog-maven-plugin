package org.enear.changelog;

/**
 * If the Git Server is not supported.
 */
public class UnknownGitServerException extends RuntimeException {

    /**
     * Creates a new Git server exception.
     */
    public UnknownGitServerException() {
        super("Unknown Git Server.");
    }
    
}
