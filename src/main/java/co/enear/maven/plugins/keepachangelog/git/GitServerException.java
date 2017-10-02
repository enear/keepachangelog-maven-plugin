package co.enear.maven.plugins.keepachangelog.git;

/**
 * If an error occurs in a Git server.
 */
public class GitServerException extends RuntimeException {

    public GitServerException(String message) {
        super(message);
    }

    public GitServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
