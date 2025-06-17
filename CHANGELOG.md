# Changelog

All notable changes to CowMurder will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.1.0] - 2025-06-13

### Added
- **Configuration System**: Comprehensive `config.yml` with customizable settings
  - Configurable punishment types: DEATH, DAMAGE, or LIGHTNING_ONLY
  - Customizable damage amounts for DAMAGE punishment type
  - Toggleable lightning effects and death messages
  - Master enable/disable switch for the plugin
  - Debug mode for detailed logging
- **Permission System**: Role-based access control
  - `cowmurder.bypass` - Allows players to harm cows without punishment
  - `cowmurder.admin` - Grants access to admin commands
- **Admin Commands**: Server management functionality
  - `/cowmurder` - Display plugin information and help
  - `/cowmurder reload` - Reload configuration without server restart
  - `/cowmurder stats <player>` - View player's cow-related statistics
- **Enhanced Death Messages**: Fully customizable with placeholder support
  - Support for `%player%` placeholder in death messages
  - Configurable message list in config.yml
- **Comprehensive Logging**: Improved debugging and error reporting
  - Debug mode with detailed operation logging
  - Proper error handling with informative messages
  - Warning logs for configuration issues
- **Scoreboard Enhancements**: More control over statistics tracking
  - Configurable objective names and display names
  - Independent control for assault and kill tracking
  - Option to completely disable scoreboard functionality

### Changed
- **Updated Minecraft Compatibility**: Now supports Minecraft 1.21.4
- **Improved Event Handling**: Replaced temporary event listeners with proper cleanup system
- **Enhanced Error Handling**: Added try-catch blocks and null checks throughout
- **Better Code Structure**: Refactored for maintainability and readability
- **Performance Improvements**: Fixed memory leaks and optimized event processing
- **Plugin.yml Updates**: Added command and permission definitions

### Fixed
- **Memory Leak**: Eliminated memory leak from temporary event listeners that weren't properly cleaned up
- **Null Pointer Exceptions**: Added comprehensive null checks for scoreboard operations
- **Event Handler Cleanup**: Proper cleanup of event handlers to prevent resource leaks
- **Scoreboard Management**: Improved scoreboard creation and management with error handling
- **Configuration Loading**: Robust configuration loading with fallback defaults

### Technical
- **Dependency Updates**: Updated Spigot API to 1.21.4-R0.1-SNAPSHOT
- **Code Quality**: Added comprehensive error handling and logging
- **Documentation**: Created detailed configuration documentation and examples
- **Testing**: Added extensive playtest documentation (PLAYTEST.md)
- **Release Process**: Created release documentation (RELEASE.md)

## [1.0.3] - 2025-01-15

### Changed
- Updated for Minecraft 1.21.1 compatibility
- Updated Spigot API dependency to 1.21-R0.1-SNAPSHOT

### Technical
- Updated README with new version compatibility information

## [1.0.2] - 2024-XX-XX

### Added
- Initial release functionality
- Basic cow protection mechanism
- Hardcoded death messages
- Simple scoreboard tracking
- Lightning effects on punishment

### Fixed
- Various bug fixes and stability improvements

## [1.0.1] - 2024-XX-XX

### Fixed
- Initial bug fixes and improvements

## [1.0.0] - 2024-XX-XX

### Added
- Initial plugin release
- Basic cow harm detection
- Player punishment system
- Death message broadcasting
- Scoreboard tracking for cow assaults and kills
- Lightning effect on punishment

---

## Release Notes Format

### Categories Used:
- **Added** for new features
- **Changed** for changes in existing functionality  
- **Deprecated** for soon-to-be removed features
- **Removed** for now removed features
- **Fixed** for any bug fixes
- **Security** for security-related changes
- **Technical** for developer-focused changes

### Version Numbering:
This project follows [Semantic Versioning](https://semver.org/):
- **MAJOR.MINOR.PATCH** (e.g., 1.1.0)
- **MAJOR**: Breaking changes or major rewrites
- **MINOR**: New features, backward compatible
- **PATCH**: Bug fixes, backward compatible

### Links:
- [Unreleased]: https://github.com/voidfemme/cow_murder/compare/v1.1.0...HEAD
- [1.1.0]: https://github.com/voidfemme/cow_murder/compare/v1.0.3...v1.1.0
- [1.0.3]: https://github.com/voidfemme/cow_murder/compare/v1.0.2...v1.0.3
- [1.0.2]: https://github.com/voidfemme/cow_murder/compare/v1.0.1...v1.0.2
- [1.0.1]: https://github.com/voidfemme/cow_murder/compare/v1.0.0...v1.0.1
- [1.0.0]: https://github.com/voidfemme/cow_murder/releases/tag/v1.0.0
