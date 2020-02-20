# Keep a changelog Maven plugin

This Maven plugin to manage changelogs that adhere to [keep a_changelog](http://keepachangelog.com/en/1.0.0/).
The plugin tasks are described next.

## Validate

This task will perform some validations on your changelog.

```bash
mvn keepachangelog:validate
```

Namely it will check:

 * If you have tags in your repository that are not in the changelog.
 * If you have versions in your changelog that do not have corresponding tags.

## Release

The release task can be executed as follows:

```bash
mvn keepachangelog:release
```

Releasing will perform the following operations:

 * Replace the `Unreleased` version with the current application version, found in `pom.xml`.
 * Add a new `Unreleased` version.
 * Update the reference links for version comparisons.

For example, if this is your `pom.xml`:

```xml
<project>
  ...
  <name>myproject</name>
  <version>1.0</version>
  ...
  <!-- Required to generate the reference links -->
  <scm>
      <connection>scm:git:git://github.com/me/myproject.git</connection>
      <url>https://github.com/me/myproject/tree/master</url>
  </scm>
</project>
```

And this is your `CHANGELOG.md`:

```markdown
# My Project

## [Unreleased]

## 0.5 - 2017-05-05

[Unreleased]: https://github.com/me/myproject/compare/v0.5..vHEAD
```

Executing the `release` task would transform `CHANGELOG.md` into:

```markdown
# My Project

## [Unreleased]

## [1.0] - 2017-08-03

## 0.5 - 2017-05-05

[Unreleased]: https://github.com/me/myproject/compare/v1.0..vHEAD
[1.0]: https://github.com/me/myproject/compare/v0.5..v1.0
```

## Release with Maven Release Plugin

Note: Unfortunately it's not working yet. The changelog release is being committed.

Most users probably want to run this plugin with the [Maven Release Plugin][maven-release-plugin]. The changelog must be
released at the end of the preparation goals.

```xml
<plugins>
    <plugin>
        <groupId>co.enear.maven.plugins</groupId>
        <artifactId>keepachangelog-maven-plugin</artifactId>
        <version>${keepachangelog.version}</version>
    </plugin>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-release-plugin</artifactId>
        <version>${maven.release.plugin}</version>
        <configuration>
            <preparationGoals>clean verify keepachangelog:release</preparationGoals>
        </configuration>
    </plugin>
</plugins>
```

This configuration can be tested in your local repository with the following command:

```sh
$ mvn release:clean release:prepare -DpushChanges=false
```

If everything ran as expected you should have two commits, one for the release and another for the next development
iteration. If not, you can always rollback and try again:

```sh
# There's a rollback goal but it doesn't work very well
$ mvn release:rollback

# Deletes two commits
# Note that prepare goal may perform 0 to 2 commits
$ mvn reset --hard HEAD~2

# Deletes the release tag
# Note that prepare goal may not have created a tag at all
$ git tag -d ${releaseVersion}
```

When you're happy finish your release:

```sh
# If you did not push your changes before, push manually
$ git push
$ git push --tags

# Finish your release
$ mvn release:perform
```

Or you might as well run everything in one go if you're feeling lucky:

```sh
$ mvn release:clean release:prepare release:perform
```

This plugin is configured to run itself before releasing a new version:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-release-plugin</artifactId>
    <version>${maven.release.plugin}</version>
    <configuration>
        <preparationGoals>clean verify co.enear.maven.plugins:keepachangelog-maven-plugin:release</preparationGoals>
        <tagNameFormat>v@{project.version}</tagNameFormat>
        <scmReleaseCommitComment>Update release @{releaseLabel}</scmReleaseCommitComment>
        <scmDevelopmentCommitComment>Update for next development iteration</scmDevelopmentCommitComment>
    </configuration>
</plugin>
```

In this project the configuration is more elaborate so that the tags follow the standard format and the source control
history is shorter. A longer reference to itself is also required.

## Options

The following options are available:

 * `repositoryUrl`: the repository web page URL. Used to generate diff links. By default read from the `scm/url` entry
   in `pom.xml`.
 * `connectionUrl`: the repository connection URL. Used to get repository information, such as tags. By default read
   from the `scm/connection` entry in `pom.xml`.
 * `rangeUrl`: a custom range URL. If defined it will be used to generate diff links. Requires a `${start}` and `${end}`
   variables, matching the reference range. Overrides the `repositoryUrl` when creating diff links. Example:
   `https://gitrepos.com/prj/compare/${start}..${end}`. 
 * `username`: the username to connect to the Git repository.
 * `password`: the password to connect to the Git repository. Encrypted passwords on `settings.xml` are supported and
   it is the recommended method for security reasons. See the Password Encryption section bellow for more information.
 * `tagFormat`: the format of a tag compared to a version. This is used to create version comparison URLs. The
   `${version}` placeholder will be replaced by versions found in the Changelog. The default value is `v${version}`
   which is the most popular Git tag format.
 * `skip`: skips the execution of this plugin.
 * `skipModules`: skips the execution of this plugin in the modules.
 * `skipRoot`: skips the execution of this plugin in the root project.
 
## Multi Module Projects

If you have a multi-module project you may want to run this plugin as follows:

- Run the plugin in the parent and modules
- Run the plugin in the parent pom only
- Run the plugin in the modules only.

The following sections show how to get each configuration running.

### Run on parent and modules

Due to the way Maven works the plugin runs on the parent and modules by default. The following configuration on the
parent pom should be enough:

```xml
<plugins>
    <plugin>
        <groupId>co.enear.maven.plugins</groupId>
        <artifactId>keepachangelog-maven-plugin</artifactId>
        <version>${keepachangelog.version}</version>
    </plugin>
</plugins>
```

### Run on parent POM only

Perhaps the most common option is to run on the parent pom only. The easiest way to achieve this is with the following
configuration on the parent pom:

```xml
<plugins>
    <plugin>
        <groupId>co.enear.maven.plugins</groupId>
        <artifactId>keepachangelog-maven-plugin</artifactId>
        <version>${keepachangelog.version}</version>
        <configuration>
            <skipModules>true</skipModules>
        </configuration>
    </plugin>
</plugins>
```

This configuration still works even if your parent project declares another parent. 

### Run on the modules only

Lastly you may want to run on the modules only. There is an option to skip the root project:

```xml
<plugins>
    <plugin>
        <groupId>co.enear.maven.plugins</groupId>
        <artifactId>keepachangelog-maven-plugin</artifactId>
        <version>${keepachangelog.version}</version>
        <configuration>
            <skipRoot>true</skipRoot>
        </configuration>
    </plugin>
</plugins>
```

However, perhaps it's best to explicitly declare the dependency on each module.

## Password Encryption

Since the `scm` entry does not support `id` you need to add the
`project.scm.id` property to your `pom.xml`:

```xml
<project>
  ...
  <properties>
    ...
    <project.scm.id>my-scm-server</project.scm.id>
    ...
  </properties>
  ...
</project>
```

If you don't have the `$~/.m2/security-settings.xml` file you need to generate
a master password: 

```bash
mvn --encrypt-master-password
```

Let's say the output is something like:

```
{jSMOWnoPFgsHVpMvz5VrIt5kRbzGpI8u+9EF1iFQyJQ=}
```

Create the `$~/.m2/security-settings.xml` file with the following content:

```xml
<settingsSecurity>
  <master>{jSMOWnoPFgsHVpMvz5VrIt5kRbzGpI8u+9EF1iFQyJQ=}</master>
</settingsSecurity>
```

Input your Git repository password after the following command:

```bash
mvn --encrypt-password
```

The command will generate an encrypted password, such as:

```
{COQLCE6DU6GtcS5P=}
```

Finally add the following entry to the `~/.m2/settings.xml` file:

```xml
<settings>
  ...
  <servers>
    ...
    <server>
      <id>my-scm-server</id>
      <username>foo</username>
      <password>{COQLCE6DU6GtcS5P=}</password>
    </server>
    ...
  </servers>
  ...
</settings>
```

More information on the [maven encryption_guide](https://maven.apache.org/guides/mini/guide-encryption.html).

## Known Issues

These are the known issues and possible fixes:

 * Only Git repositories are supported.
 * Only GitHub and BitBucket are supported.
 * Missing custom URLs for diff links
 * The Changelog syntax is not checked. Could be fixed by parsing the Markdown and checking if it corresponds to a
   Changelog.

[maven-release-plugin]:http://maven.apache.org/maven-release/maven-release-plugin/