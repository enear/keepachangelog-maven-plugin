# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Skip root configuration.
- Skip modules configuration.

## [1.3.0] - 2019-11-10

### Added

- Skip configuration.

### Fixed

- Uncaught exception when the changelog was not found. Besides throwing an exception it would have the undesired
  secondary effect of creating an empty file. When the changelog is not found it will simply show a warning. Perhaps it
  would be a good idea to have an option to fail on error in the future.
- Uncaught exceptions when the SCM was undefined.

## [1.2.3] - 2019-09-13

### Fixed

- Every reference link was being deleted in order to generate version diff reference links at the end of the file.
  This would mean that a user could not have it's own reference links. Instead the only reference links that are
  deleted are the ones that correspond to detected versions. 

## [1.2.2] - 2018-05-29

### Changed

- When an unknown server was detected the release command would not
  complete but there is no logical reason for that to happen as a change
  log does not have to have a diff list. Instead if a server is unknown the
  release command simply does not write the diff list.

## [1.2.1] - 2017-08-18

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

[Unreleased]: https://github.com/enear/keepachangelog-maven-plugin/compare/v1.3.0...HEAD
[1.3.0]: https://github.com/enear/keepachangelog-maven-plugin/compare/v1.2.3...v1.3.0
[1.2.3]: https://github.com/enear/keepachangelog-maven-plugin/compare/v1.2.2...v1.2.3
[1.2.2]: https://github.com/enear/keepachangelog-maven-plugin/compare/v1.2.1...v1.2.2
[1.2.1]: https://github.com/enear/keepachangelog-maven-plugin/compare/v1.2.0...v1.2.1
[1.2.0]: https://github.com/enear/keepachangelog-maven-plugin/compare/v1.1.1...v1.2.0
[1.1.1]: https://github.com/enear/keepachangelog-maven-plugin/compare/v1.1.0...v1.1.1
[1.1.0]: https://github.com/enear/keepachangelog-maven-plugin/compare/v1.0.0...v1.1.0
