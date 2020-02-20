package co.enear.maven.plugins.keepachangelog;

/*-
 * #%L
 * keepachangelog-maven-plugin
 * %%
 * Copyright (C) 2017 - 2018 e.near
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
    protected String connectionUrl;

    @Parameter(property = "repositoryUrl")
    protected URL repositoryUrl;
    
    @Parameter(property = "rangeUrl")
    protected String rangeUrl;

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

    @Parameter(defaultValue = "false", readonly = true)
    protected boolean skip;

    @Parameter(defaultValue = "false", readonly = true)
    protected boolean skipModules;

    @Parameter(defaultValue = "false", readonly = true)
    protected boolean skipRoot;

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
     */
    private void initConnectionURLFromPom() {
        connectionUrl = MavenUtils.getScmConnectionUrl(project)
                .map(c -> c.replaceFirst("scm:[^:]+:", ""))
                .orElse(null);
    }

    /**
     * Initializes the repository URL using Maven POM.
     */
    private void initRepositoryURLFromPom() {
        repositoryUrl = MavenUtils.getScmUrl(project).map(c -> {
            try {
                return new URL(c);
            } catch (MalformedURLException e) {
                return null;
            }
        }).orElse(null);
    }

    /**
     * Returns the path of the changelog. The changelog should be in the same directory
     * as the Maven POM and be called {@value CHANGELOG_FILENAME}.
     *
     * @return the path of the changelog.
     */
    Path getChangelogPath() {
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

        if (repositoryUrl == null) {
            initRepositoryURLFromPom();
        }
    }
}
