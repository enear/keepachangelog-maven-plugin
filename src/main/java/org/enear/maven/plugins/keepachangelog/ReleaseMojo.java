package org.enear.maven.plugins.keepachangelog;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.enear.maven.plugins.keepachangelog.git.RepoServer;
import org.enear.maven.plugins.keepachangelog.git.GitServerException;
import org.enear.maven.plugins.keepachangelog.git.GitServerFactory;
import org.enear.maven.plugins.keepachangelog.markdown.generic.RefLink;
import org.enear.maven.plugins.keepachangelog.markdown.specific.ChangelogReader;
import org.enear.maven.plugins.keepachangelog.markdown.specific.DiffRefLink;
import org.enear.maven.plugins.keepachangelog.markdown.specific.ReaderException;
import org.enear.maven.plugins.keepachangelog.markdown.specific.VersionHeading;
import org.enear.maven.plugins.keepachangelog.utils.Range;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private static final String CHANGELOG_TMP_PREFIX = "changelog";
    private static final String CHANGELOG_TMP_SUFFIX = ".tmp";

    private List<String> parsedVersions = new ArrayList<>();

    /**
     * Replaces the unreleased currVersion with the current application currVersion and add a new unreleased currVersion.
     *
     * @param currVersion the current currVersion of the application.
     * @param bw          the writer for the updated changelog.
     * @throws IOException if an I/O error occurs.
     */
    private void writeNewVersion(String currVersion, boolean refLink, BufferedWriter bw) throws IOException {
        VersionHeading unrelVerHeading = VersionHeading.unreleased(true);
        VersionHeading currVerHeading = new VersionHeading(currVersion, LocalDate.now(), refLink);

        bw.write(unrelVerHeading.toMarkdown());
        bw.newLine();
        bw.newLine();
        bw.write(currVerHeading.toMarkdown());
        bw.newLine();
    }

    /**
     * Writes a list of version comparison links.
     *
     * @param repoServer the git server
     * @param tagRange  the tag ranges.
     * @param bw        the buffered writer.
     * @throws IOException if an error occurs while writing.
     */
    private void writeDiffLink(RepoServer repoServer, Range<String> tagRange, BufferedWriter bw) throws IOException {
        DiffRefLink diffRefLink = new DiffRefLink(tagFormat, repoServer, tagRange);
        bw.write(diffRefLink.toMarkdown());
        bw.newLine();
    }

    /**
     * Writes links to version differences based on tag ranges and a git server.
     *
     * @param repoServer the git server where the URL will be used to write the Git version differences.
     * @param bw        the writer for the updated changelog.
     * @throws IOException if an I/O error occurs.
     */
    private void writeDiffLinks(RepoServer repoServer, BufferedWriter bw) throws IOException {
        if ( repoServer != null ) {
            List<Range<String>> tagRanges = getTagRanges();
            for (Range<String> tagRange : tagRanges) {
                writeDiffLink(repoServer, tagRange, bw);
            }
        }
    }

    /**
     * Updates a changelog.
     *
     * @param currVersion the current currVersion of the application.
     * @param gitServer   the Git server.
     * @param path        the path of the changelog to update.
     * @throws MojoFailureException if an error occur.
     */
    private void writeNewChangelog(String currVersion, RepoServer gitServer,
                                   Path path) throws MojoFailureException {
        Path temp = createTempChangelog();
        try (BufferedWriter bw = Files.newBufferedWriter(temp)) {
            ChangelogReader reader = new ChangelogReader() {
                @Override
                protected void onVersionHeading(VersionHeading versionHeading) {
                    try {
                        if (versionHeading.isUnreleased()) {
                            boolean refLink = versionHeading.isRefLink();
                            writeNewVersion(currVersion, refLink, bw);
                            parsedVersions.add(UNRELEASED_VERSION);
                            parsedVersions.add(currVersion);
                        } else {
                            bw.write(versionHeading.toMarkdown());
                            bw.newLine();
                            String version = versionHeading.getVersion();
                            parsedVersions.add(version);
                        }
                    } catch (Exception e) {
                        throw new ReaderException("Failed to read version heading", e);
                    }
                }

                @Override
                protected void onRefLink(RefLink refLink) {
                }

                @Override
                protected void onOther(String line) {
                    try {
                        bw.write(line);
                        bw.newLine();
                    } catch (Exception e) {
                        throw new ReaderException("Failed to read line", e);
                    }
                }
            };
            reader.read(path);
            writeDiffLinks(gitServer, bw);
        } catch (Exception e) {
            throw new MojoFailureException("Failed to replace the original changelog.", e);
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
     * @throws GitServerException if an error occurs while getting the origin repository.
     */
    private RepoServer getOriginRepo(URL url) {
        return GitServerFactory.from(url);
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
     * @return the ranges of tags in reverse order.
     */
    private List<Range<String>> getTagRanges() {
        List<String> begins = new ArrayList<>(parsedVersions);
        if (!begins.isEmpty()) {
            List<String> ends = new ArrayList<>(begins);
            begins.remove(0);
            ends.remove(ends.size() - 1);
            List<Range<String>> ranges = Range.zip(begins, ends);

            return ranges;
        } else {
            return Collections.EMPTY_LIST;
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
        RepoServer origin = getOriginRepo(connectionUrl);
        writeNewChangelog(version, origin, path);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        super.execute();
        writeNewChangelog();
    }
}
