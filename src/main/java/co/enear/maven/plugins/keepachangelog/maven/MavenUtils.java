package co.enear.maven.plugins.keepachangelog.maven;

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
