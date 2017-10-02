package co.enear.maven.plugins.keepachangelog;

import co.enear.maven.plugins.keepachangelog.git.TagUtils;
import co.enear.maven.plugins.keepachangelog.maven.MavenUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.crypto.SettingsDecrypter;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

/**
 * An abstract class common with functions common Mojos.
 */
public class InitMojo extends AbstractMojo {

    public static final String UNRELEASED_VERSION = "Unreleased";

    private static final String CHANGELOG_FILENAME = "CHANGELOG.md";
    private static final String SERVER_ID_KEY = "project.scm.id";

    @Parameter(property = "connectionUrl")
    protected URL connectionUrl;

    @Parameter(property = "username")
    protected String username;

    @Parameter(property = "password")
    protected String password;

    @Parameter(defaultValue = "${project}", readonly = true)
    protected MavenProject project;

    @Parameter(defaultValue = "${settings}", readonly = true)
    protected Settings settings;

    @Parameter(defaultValue = TagUtils.DEFAULT_TAG_FORMAT, readonly = true)
    protected String tagFormat;

    @Component(role = SettingsDecrypter.class)
    protected SettingsDecrypter settingsDecrypter;

    /**
     * The server ID configured in the {@value SERVER_ID_KEY} property.
     *
     * @return the server ID.
     */
    private String getServerId() {
        return project.getProperties().getProperty(SERVER_ID_KEY);
    }

    /**
     * Initializes the credentials using Maven settings.
     */
    private void initCredentialsFromSettings() {
        String serverId = getServerId();
        if (serverId != null) {
            MavenUtils.decrypt(settings, settingsDecrypter, serverId).ifPresent(decryptedServer -> {
                        username = decryptedServer.getUsername();
                        password = decryptedServer.getPassword();
                    }
            );
        }
    }

    /**
     * Initializes the connection URL using Maven POM.
     *
     * @throws MojoFailureException
     */
    private void initConnectionURLFromPom() throws MojoFailureException {
        try {
            MavenUtils.getScmConnectionUrl(project).ifPresent(c -> connectionUrl = c);
        } catch (MalformedURLException e) {
            throw new MojoFailureException("Failed to get connection URL.", e);
        }
    }

    /**
     * Returns the path of the changelog. The changelog should be in the same directory
     * as the Maven POM and be called {@value CHANGELOG_FILENAME}.
     *
     * @return the path of the changelog.
     */
    protected Path getChangelogPath() {
        Path basedir = project.getBasedir().toPath();
        return basedir.resolve(CHANGELOG_FILENAME);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (username == null && password == null) {
            initCredentialsFromSettings();
        }

        if (connectionUrl == null) {
            initConnectionURLFromPom();
        }
    }
}
