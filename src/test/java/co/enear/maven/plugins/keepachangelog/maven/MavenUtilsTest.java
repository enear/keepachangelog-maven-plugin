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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

import java.net.MalformedURLException;
import java.util.Optional;

import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.apache.maven.settings.crypto.SettingsDecryptionResult;
import org.junit.Test;
import org.mockito.Mockito;

public class MavenUtilsTest {


    @Test
    public void getScmConnectionUrlTest() throws MalformedURLException {
        //		MavenProject mvnProject = new MavenProject();
        //		
        //		Model model = new Model();
        //		Scm scm = new Scm();
        //		scm.setConnection(CONNECTION);
        //		model.setScm(scm);
        //		
        //		mvnProject.getModel().setScm(scm);
        //		
        //		Optional<URL> actual = MavenUtils.getScmConnectionUrl(mvnProject);
    }

    @Test
    public void decryptOptionalEmptyTest() {
        Settings settings = new Settings();
        SettingsDecrypter settingsDecrypter = Mockito.mock( SettingsDecrypter.class );
        String serverId = "0";

        Optional<Server> actual = MavenUtils.decrypt(settings, settingsDecrypter, serverId);
        assertFalse(actual.isPresent());
    }

    @Test
    public void decryptTest() {
        Settings settings = Mockito.mock( Settings.class );
        SettingsDecrypter settingsDecrypter = Mockito.mock( SettingsDecrypter.class );
        SettingsDecryptionResult settingsDecryptionResult = Mockito.mock( SettingsDecryptionResult.class );
        Server server = new Server();

        Mockito.when( settings.getServer( anyString() ) ).thenReturn( server );
        Mockito.when( settingsDecrypter.decrypt( any() ) ).thenReturn( settingsDecryptionResult );
        Mockito.when( settingsDecryptionResult.getServer() ).thenReturn( server );

        Optional<Server> actual = MavenUtils.decrypt(settings, settingsDecrypter, "0");
        assertTrue(actual.isPresent());
    }

}
