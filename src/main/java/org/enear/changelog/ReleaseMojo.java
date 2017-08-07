package org.enear.changelog;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Releases a changelog.
 * <p>
 * Executes the following steps:
 * <ul>
 * <li>Replaces the unreleased version with the current version.</li>
 * <li>Adds a new unreleased version.</li>
 * <li>Updates the tag comparison links based on the repository tags.</li>
 * </ul>
 */
@Mojo(name = "release")
public class ReleaseMojo extends InitMojo {

    private static final String UNRELEASED_VERSION = "Unreleased";
    private static final String CHANGELOG_TMP_PREFIX = "changelog";
    private static final String CHANGELOG_TMP_SUFFIX = ".tmp";
    private static final String UNRELEASED_REGEX = "^## \\[Unreleased\\]$";
    private static final String DIFF_URL_REGEX = "^\\[.*\\]: .*$";

    private static final Pattern unreleasedPattern = Pattern.compile(UNRELEASED_REGEX);
    private static final Pattern diffUrlPattern = Pattern.compile(DIFF_URL_REGEX);

    private boolean diffsWritten = false;

    /**
     * Replaces the unreleased version with the current application version and add a new unreleased version.
     *
     * @param version the current version of the application.
     * @param bw      the writer for the updated changelog.
     * @throws IOException if an I/O error occurs.
     */
    private void writeNewVersion(String version, BufferedWriter bw) throws IOException {
        bw.write(String.format("## [%s]", UNRELEASED_VERSION));
        bw.newLine();
        bw.newLine();
        bw.write(String.format("## [%s] - %s", version, LocalDate.now()));
        bw.newLine();
    }

    /**
     * Writes links to version differences based on tag ranges and a git server.
     *
     * @param tagRanges the tag ranges to write the Git version differences.
     * @param gitServer the git server where the URL will be used to write the Git version differences.
     * @param bw        the writer for the updated changelog.
     * @throws IOException if an I/O error occurs.
     */
    private void writeDiffLinks(List<Range<String>> tagRanges, GitServer gitServer, BufferedWriter bw) throws IOException {
        if (!diffsWritten) {
            for (Range<String> tagRange : tagRanges) {
                String begin = tagRange.getBegin();
                String end = tagRange.getEnd();
                URL diffUrl = gitServer.diff(begin, end);
                bw.write(String.format("[%s]: %s", end, diffUrl));
                bw.newLine();
            }
            diffsWritten = true;
        }
    }

    /**
     * Writes an updated changelog line.
     *
     * @param version   the current version of the application.
     * @param tagRanges the tag ranges to write the Git version differences.
     * @param gitServer the git server where the URL will be used to write the Git version differences.
     * @param bw        the writer for the updated changelog.
     * @param line      the line of the original changelog file.
     * @throws MojoFailureException if an error occur.
     */
    private void writeNewLine(String version, List<Range<String>> tagRanges, GitServer gitServer,
                              BufferedWriter bw, String line) throws MojoFailureException {
        try {
            if (unreleasedPattern.matcher(line).matches()) {
                writeNewVersion(version, bw);
            } else if (diffUrlPattern.matcher(line).matches()) {
                writeDiffLinks(tagRanges, gitServer, bw);
            } else {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new MojoFailureException("Failed to write a new line to the updated changelog.", e);
        }
    }

    /**
     * Updates a changelog.
     *
     * @param version   the current version of the application.
     * @param tagRanges the tag ranges in reverse order.
     * @param gitServer the Git server.
     * @param path      the path of the changelog to update.
     * @throws MojoFailureException if an error occur.
     */
    private void writeNewChangelog(String version, List<Range<String>> tagRanges, GitServer gitServer,
                                   Path path) throws MojoFailureException {
        Path temp = createTempChangelog();
        try (BufferedReader pathReader = Files.newBufferedReader(path);
             BufferedWriter tempWriter = Files.newBufferedWriter(temp)) {
            String line;
            while ((line = pathReader.readLine()) != null) {
                writeNewLine(version, tagRanges, gitServer, tempWriter, line);
            }

            // If the changelog does not contain any diff links yet
            if (!diffsWritten) {
                writeDiffLinks(tagRanges, gitServer, tempWriter);
                diffsWritten = true;
            }
        } catch (IOException e) {
            throw new MojoFailureException("Failed write the updated changelog.", e);
        }

        try {
            Files.move(temp, path, REPLACE_EXISTING);
        } catch (IOException e) {
            throw new MojoFailureException("Failed to replace the original changelog.", e);
        }
    }

    /**
     * Creates a temporary changelog file.
     *
     * @return the temporary changelog path.
     * @throws MojoFailureException if an error occurs.
     */
    private Path createTempChangelog() throws MojoFailureException {
        try {
            return Files.createTempFile(CHANGELOG_TMP_PREFIX, CHANGELOG_TMP_SUFFIX);
        } catch (IOException e) {
            throw new MojoFailureException("Failed to create temporary file.", e);
        }
    }

    /**
     * Returns the origin repository.
     *
     * @param url the origin URL
     * @return the origin repository.
     * @throws MojoFailureException if an error occurs while getting the origin repository.
     */
    private GitServer getOriginRepo(URL url) throws MojoFailureException {
        try {
            return GitServerFactory.from(url);
        } catch (IOException e) {
            throw new MojoFailureException("Failed to get origin repository.", e);
        }
    }

    /**
     * Returns the ranges of tags in reverse order.
     * <p>
     * For example, if the Git repository has the following tags:
     * <p>
     * <pre>{@code
     * v1.0
     * v2.0
     * v3.1
     * }
     * </pre>
     * <p>
     * This method will return:
     * <p>
     * <pre>{@code
     * List((v3.1, Unreleased), (v2.0, v3.1), (v1.0, v2.0))
     * }
     * </pre>
     *
     * @param repoUrl  the repository URL.
     * @param username the username for the repository URL.
     * @param password the password for the repository URL.
     * @return the ranges of tags in reverse order.
     * @throws MojoFailureException if any error occurs.
     */
    private List<Range<String>> getReversedTagRanges(URL repoUrl, String username, String password)
            throws MojoFailureException {
        try {
            List<String> begins = GitUtils.getTags(repoUrl, username, password);
            if (!begins.isEmpty()) {
                List<String> ends = new ArrayList<>(begins);
                ends.remove(0);
                ends.add(UNRELEASED_VERSION);
                List<Range<String>> ranges = Range.zip(begins, ends);
                Collections.reverse(ranges);
                return ranges;
            } else {
                return Collections.EMPTY_LIST;
            }
        } catch (GitAPIException e) {
            throw new MojoFailureException("Failed to get tags.", e);
        }
    }

    /**
     * Returns the application version.
     *
     * @return the application version.
     * @throws MojoFailureException if an error occurs.
     */
    private String getAppVersion() throws MojoFailureException {
        return project.getModel().getVersion();
    }

    private void writeNewChangelog() throws MojoFailureException {
        /*
         * We want these conditions to fail as soon as possible:
         * - CHANGELOG.md must exist
         * - The pom.xml must have a version
         * - The git repo must have tags
         * - The git server must exist and be known
         */

        Path path = getChangelogPath();
        String version = getAppVersion();
        List<Range<String>> tagRanges = getReversedTagRanges(connectionUrl, username, password);
        GitServer origin = getOriginRepo(connectionUrl);
        writeNewChangelog(version, tagRanges, origin, path);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        super.execute();
        writeNewChangelog();
    }
}
