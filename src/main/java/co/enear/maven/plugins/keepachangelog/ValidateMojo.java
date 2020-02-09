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

import co.enear.maven.plugins.keepachangelog.markdown.specific.ChangelogValidator;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static co.enear.maven.plugins.keepachangelog.git.TagUtils.toTag;

@Mojo(name = "validate")
public class ValidateMojo extends InitMojo {

    private static final Logger logger = LoggerFactory.getLogger(ValidateMojo.class);

    /**
     * Warns the user that some tags in the repository do not have a matching version in the changelog.
     *
     * @param validator the changelog validator
     */
    private void warnTagsWithoutVersions(ChangelogValidator validator) {
        Set<String> tagsWithoutVersions = validator.getTagsWithoutVersions();
        if (!tagsWithoutVersions.isEmpty()) {
            logger.warn("Some tags in the repository do not have a matching version in the changelog");
        }
        for (String version : tagsWithoutVersions) {
            getLog().warn("Missing version: " + version);
        }
    }

    /**
     * Warns the user that some versions in the changelog do not have matching tags in the repository.
     *
     * @param validator the changelog validator
     */
    private void warnVersionsWithoutTags(ChangelogValidator validator) {
        Set<String> versionsWithoutTags = validator.getVersionsWithoutTags();
        if (!versionsWithoutTags.isEmpty()) {
            logger.warn("Some versions in the changelog to not have a matching tag in the repository");
        }
        for (String version : versionsWithoutTags) {
            String tag = toTag(tagFormat, version);
            getLog().warn("Missing tag: " + tag);
        }
    }

    /**
     * Validates a changelog file.
     *
     * @param path the path of the changelog
     */
    private void validate(Path path) {
        if (!Files.isRegularFile(path)) {
            logger.warn("Changelog file not found. Skipping validation.");
            return;
        }

        ChangelogValidator validator = new ChangelogValidator(connectionUrl, username, password, tagFormat);
        validator.read(path);
        warnTagsWithoutVersions(validator);
        warnVersionsWithoutTags(validator);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        super.execute();
        if (skip) return;
        if (skipModules && !project.isExecutionRoot()) return;
        if (skipRoot && project.isExecutionRoot()) return;
        Path path = getChangelogPath();
        validate(path);
    }
}
