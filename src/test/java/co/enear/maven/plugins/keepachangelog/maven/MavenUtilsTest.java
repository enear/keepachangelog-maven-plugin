package co.enear.maven.plugins.keepachangelog.maven;

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
