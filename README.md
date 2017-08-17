# Changelog Maven Plugin

This Maven plugin to manage changelogs that adheres to [Keep a
Changelog](http://keepachangelog.com/en/1.0.0/). For now there is only one
task: release.

## Release

The release task can be executed as follows:

```
mvn changelog:release
```

Releasing will perform the following operations:

 * Replace the `Unreleased` version with the current application version, found
   in `pom.xml`.
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
      <connection>scm:git:https://github.com/me/myproject.git</connection>
  </scm>
</project>
```

And this is your `CHANGELOG.md`:

```markdown
# My Project

## [Unreleased]

## [0.5] - 2017-05-05

[Unreleased]: https://github.com/me/myproject/compare/v0.5..vHEAD
```

Executing the `release` task would transform `CHANGELOG.md` into:

```markdown
# My Project

## [Unreleased]

## [1.0] - 2017-08-03

## [0.5] - 2017-05-05

[Unreleased]: https://github.com/me/myproject/compare/v1.0..vHEAD
[1.0]: https://github.com/me/myproject/compare/v0.5..v1.0
```

Additionally can use the following options:

 * `connectionUrl`: the connection URL for the Git repository. By default the
   connection URL is read from the `scm/connection` entry in `pom.xml`.
 * `tagFormat`: the format of a tag compared to a version. This is used to
   create version comparison URLs. The `${version}` placeholder will be
   replaced by versions found in the Changelog. The default value is
   `v${version}` which is the most popular Git tag format.

## Known Issues

These are the known issues and possible fixes:

 * Only Git repositories are supported.
 * Only GitHub and BitBucket are supported.
 * The Changelog syntax is not checked. Could be fixed by parsing the Markdown
   and checking if it corresponds to a Changelog.
 * The Changelog may have versions that are not tags in the Git repository.
   Could be fixed by comparing Git tags with Changelog versions.
 * Every reference link is deleted even if it's not referencing a version.
   Could be fixed by checking if the reference is a known version.
