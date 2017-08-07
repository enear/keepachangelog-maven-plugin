package org.enear.changelog;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Git utilities.
 */
public class GitUtils {

    private static String getTagName(Ref tagRef) {
        return tagRef.getName().replaceFirst("refs/tags/", "");
    }

    /**
     * Returns the tags of the given Git repository.
     *
     * @param url      the Git repository URL.
     * @param username the username to connect to the URL.
     * @param password the password to connect to the URL.
     * @return a list of tags.
     * @throws GitAPIException if an error occurs while calling a Git command
     */
    public static List<String> getTags(URL url, String username, String password)
            throws GitAPIException {
        LsRemoteCommand cmd = Git.lsRemoteRepository();
        cmd.setRemote(url.toString());
        if (username != null && password != null) {
            cmd.setCredentialsProvider(
                    new UsernamePasswordCredentialsProvider(username, password)
            );
        }
        Collection<Ref> refs = cmd.call();
        return refs.stream()
                .filter(ref -> ref.getName().startsWith("refs/tags/"))
                .map(tag -> getTagName(tag))
                .collect(Collectors.toList());
    }

}
