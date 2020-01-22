# Keep a changelog Maven plugin

This Maven plugin to manage changelogs that adheres to [keep a
changelog](http://keepachangelog.com/en/1.0.0/). The plugin tasks are described
next.

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

## Options

The following options are available:

 * `repositoryUrl`: the repository web page URL. Used to generate diff links. By default read from `scm/url` entry in
   `pom.xml`. Example:   
 * `connectionUrl`: the repository connection URL. By default read from the `scm/connection` entry in `pom.xml`.
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

More information on the [maven encryption
guide](https://maven.apache.org/guides/mini/guide-encryption.html).

## Known Issues

These are the known issues and possible fixes:

 * Only Git repositories are supported.
 * Only GitHub and BitBucket are supported.
 * Missing custom URLs for diff links
 * The Changelog syntax is not checked. Could be fixed by parsing the Markdown and checking if it corresponds to a
   Changelog.
