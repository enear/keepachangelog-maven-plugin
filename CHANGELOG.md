# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Fixed

- First version is not a reference link. All versions have a link comparing
  with the previous version but the first version as no previous version. So
  instead of having `## [1.0.0]` as a first version heading it should be `##
  1.0.0` instead.

## [1.2.0] - 2017-08-18

### Fixed

- Discrepancy in documentation.
- The version reference links should be comparing the previous version instead
  of the next version.

## [1.1.1] - 2017-08-18

### Fixed

- Broken GitHub version comparison URLs.

## [1.1.0] - 2017-08-17

### Added

- Task to validate a given changelog

### Fixed

- Fix used undeclared dependencies

## 1.0.0 - 2017-08-17

### Added

- Task to release a given changelog

[Unreleased]: https://github.com/enear/changelog-maven-plugin/compare/v1.2.0...HEAD
[1.2.0]: https://github.com/enear/changelog-maven-plugin/compare/v1.1.1...v1.2.0
[1.1.1]: https://github.com/enear/changelog-maven-plugin/compare/v1.1.0...v1.1.1
[1.1.0]: https://github.com/enear/changelog-maven-plugin/compare/v1.0.0...v1.1.0
