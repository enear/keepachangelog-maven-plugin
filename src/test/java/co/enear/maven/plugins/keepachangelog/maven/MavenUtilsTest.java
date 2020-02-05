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

import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.apache.maven.settings.crypto.SettingsDecryptionResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MavenUtilsTest {

    @Mock
    private Settings settings;

    @Mock
    private SettingsDecrypter settingsDecrypter;

    @Mock
    private SettingsDecryptionResult settingsDecryptionResult;

    @Test
    public void should_GetNothing_WhenServerIsNotConfigured() {
        when(settings.getServer(anyString())).thenReturn(null);

        Optional<Server> server = MavenUtils.decrypt(settings, settingsDecrypter, "myServer");
        assertFalse(server.isPresent());
    }

    @Test
    public void should_GetServerWithDecryptedPassword_WhenServerIsConfigured() {
        Server server = new Server();

        when(settings.getServer(anyString())).thenReturn(server);
        when(settingsDecrypter.decrypt(any())).thenReturn(settingsDecryptionResult);
        when(settingsDecryptionResult.getServer()).thenReturn(server);

        Optional<Server> actual = MavenUtils.decrypt(settings, settingsDecrypter, "myServer");
        assertEquals(Optional.of(server), actual);
    }

}
