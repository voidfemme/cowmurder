# CowMurder

CowMurder is a highly configurable Minecraft plugin that adds a humorous twist to cow interactions. It punishes players for harming cows with customizable consequences and keeps detailed statistics of cow-related offenses.

## Features

### Core Functionality
- **Configurable Punishment**: Choose between instant death, damage, or lightning-only effects
- **Custom Death Messages**: Fully customizable humorous death messages with player placeholders
- **Lightning Effects**: Dramatic visual lightning strikes (cosmetic only - won't harm cows)
- **Scoreboard Tracking**: Comprehensive tracking of cow assaults and kills

### Administrative Features
- **Permission System**: Bypass protection for admins and trusted players
- **Admin Commands**: Reload configuration and view player statistics
- **Comprehensive Logging**: Debug mode for troubleshooting
- **Error Handling**: Robust error handling with informative logging

### Customization Options
- **Multiple Punishment Types**: Death, damage amounts, or visual effects only
- **Configurable Messages**: Edit all death messages to match your server's style
- **Scoreboard Control**: Enable/disable tracking components independently
- **Flexible Settings**: Toggle lightning effects, debug mode, and more

## Installation

1. Ensure you have a Spigot or Paper Minecraft server (version 1.21.4 or compatible) set up.
2. Download the latest `CowMurder.jar` from the [releases section](https://github.com/voidfemme/cow_murder/releases).
3. Place the JAR file in your server's `plugins` folder.
4. Restart your server.
5. Edit `plugins/CowMurder/config.yml` to customize settings (optional).
6. Use `/cowmurder reload` to apply configuration changes without restarting.

## Building from Source

To build the plugin from source:
1. Ensure you have Java 21 and Gradle installed.
2. Clone this repository.
3. Run `./gradlew clean build` in the project root directory.
4. The compiled JAR will be in the `build/libs` folder.

## Usage

The plugin works automatically once installed. Players will be punished for harming cows according to your configuration settings, and their offenses will be tracked on the scoreboard.

### Default Behavior
- Players who hit cows are instantly killed
- Lightning effect appears at punishment location
- Custom death messages are broadcast
- Statistics are tracked on server scoreboard

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/cowmurder` | Show plugin info and help | `cowmurder.admin` |
| `/cowmurder reload` | Reload configuration file | `cowmurder.admin` |
| `/cowmurder stats <player>` | View player's cow-related statistics | `cowmurder.admin` |

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `cowmurder.bypass` | Bypass cow protection (can harm cows) | `op` |
| `cowmurder.admin` | Access to admin commands | `op` |

## Configuration

The plugin creates a comprehensive `config.yml` file with the following options:

### Punishment Settings
```yaml
settings:
  punishment-type: DEATH        # DEATH, DAMAGE, or LIGHTNING_ONLY
  damage-amount: 10.0          # If using DAMAGE type
  lightning-effect: true       # Enable lightning visual effect
  custom-death-messages: true  # Use custom death messages
```

### Death Messages
```yaml
death-messages:
  - "%player% was moo-rdered for their bovine crimes"
  - "%player% faced divine bovine retribution"
  # Add more custom messages...
```

### Scoreboard Options
```yaml
scoreboard:
  enabled: true           # Enable scoreboard tracking
  track-assaults: true    # Track cow damage events
  track-kills: true       # Track cow death events
```

### Debug and Logging
```yaml
settings:
  debug: false           # Enable detailed console logging
  enabled: true          # Master enable/disable switch
```

For complete configuration options, see the generated `config.yml` file.

## Compatibility

- **Minecraft Version**: 1.21.4
- **Server Software**: Spigot, Paper, and derivatives
- **Java Version**: 21 or higher

## Recent Updates

### Version 1.1.0 (Latest)
- **Major Overhaul**: Complete rewrite with configuration system
- **Permission System**: Added bypass and admin permissions
- **Admin Commands**: Configuration reload and player statistics
- **Enhanced Logging**: Debug mode and comprehensive error handling
- **Configurable Punishment**: Multiple punishment types and customization
- **Memory Leak Fixes**: Improved event handling and cleanup
- **Updated Compatibility**: Minecraft 1.21.4 support

### Previous Versions
- Version 1.0.3: Updated for Minecraft 1.21.1 compatibility

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE)

## Support

For support, please open an issue on the GitHub repository.

## Disclaimer

Use this plugin at your own risk. The author is not responsible for any damage to your server, player frustration, or the inevitable cow uprising that may result from their newfound invincibility. Cows, on the other hand, may send thank-you notes.
