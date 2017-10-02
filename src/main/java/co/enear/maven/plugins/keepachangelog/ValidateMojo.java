package co.enear.maven.plugins.keepachangelog;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import co.enear.maven.plugins.keepachangelog.markdown.specific.ChangelogValidator;

import java.nio.file.Path;
import java.util.Set;

import static co.enear.maven.plugins.keepachangelog.git.TagUtils.toTag;

@Mojo(name = "validate")
public class ValidateMojo extends InitMojo {

    private void handleTagsWithoutVersions(ChangelogValidator validator) {
        Set<String> tagsWithoutVersions = validator.getTagsWithoutVersions();
        for (String version : tagsWithoutVersions) {
            getLog().warn("Missing version: " + version);
        }
    }

    private void handleVersionsWithoutTags(ChangelogValidator validator) {
        Set<String> versionsWithoutTags = validator.getVersionsWithoutTags();
        for (String version : versionsWithoutTags) {
            String tag = toTag(tagFormat, version);
            getLog().warn("Missing tag: " + tag);
        }
    }

    public void validate(Path path) {
        ChangelogValidator validator = new ChangelogValidator(connectionUrl, username, password, tagFormat);
        validator.read(path);
        handleTagsWithoutVersions(validator);
        handleVersionsWithoutTags(validator);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        super.execute();
        Path path = getChangelogPath();
        validate(path);
    }
}
