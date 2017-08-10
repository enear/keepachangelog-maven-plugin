package org.enear.changelog.maven;

import org.apache.maven.model.Model;
import org.apache.maven.model.Scm;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.crypto.DefaultSettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.apache.maven.settings.crypto.SettingsDecryptionResult;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * Maven utilities.
 */
public class MavenUtils {

    /**
     * Returns the SCM connection URL,
     *
     * @param model the Maven model to extract the SCM connection URL from.
     * @return the SCM connection URL.
     * @throws MalformedURLException if the SCM connection URL is malformed.
     */
    private static Optional<URL> getScmConnectionUrl(Model model) throws MalformedURLException {
        Scm scm = model.getScm();
        if (scm != null) {
            String connection = scm.getConnection();
            if (connection != null) {
                return Optional.of(new URL(connection.replaceFirst("scm:[^:]+:", "")));
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the SCM connection URL.
     *
     * @param project the Maven project to extract the SCM connection URL from.
     * @return the SCM connection URL.
     * @throws MalformedURLException if the SCM connection URL is malformed.
     */
    public static Optional<URL> getScmConnectionUrl(MavenProject project) throws MalformedURLException {
        Model model = project.getModel();
        return getScmConnectionUrl(model);
    }

    /**
     * Returns a Server with the password decrypted.
     *
     * @param settings          the Maven settings.
     * @param settingsDecrypter the Maven settings decrypter.
     * @param serverId          the ID of the server.
     * @return a Server with the password decrypted.
     */
    public static Optional<Server> decrypt(Settings settings, SettingsDecrypter settingsDecrypter, String serverId) {
        Server server = settings.getServer(serverId);
        if (server != null) {
            DefaultSettingsDecryptionRequest request = new DefaultSettingsDecryptionRequest(server);
            SettingsDecryptionResult result = settingsDecrypter.decrypt(request);
            return Optional.of(result.getServer());
        } else {
            return Optional.empty();
        }
    }

}
