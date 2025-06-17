# CowMurder Plugin Playtest Instructions

## Pre-Test Setup

1. **Install the Plugin**
   - Copy `cowmurder-1.0.3.jar` to your server's `plugins` folder
   - Start/restart your Minecraft server (1.21.4)
   - Verify plugin loaded: Check console for "CowMurder has been enabled!"

2. **Check Generated Files**
   - Verify `plugins/CowMurder/config.yml` was created
   - Review default configuration settings

## Test Scenarios

### 1. Basic Functionality Tests

#### Test 1.1: Default Cow Protection
- **Setup**: Use a regular player (not op)
- **Action**: Spawn a cow, try to hit it with any weapon/tool
- **Expected**: 
  - Player dies instantly
  - Lightning effect appears
  - Custom death message displays
  - Cow takes no damage
  - Assault tracked in scoreboard

#### Test 1.2: Cow Killing (if possible)
- **Setup**: Use creative mode or commands to kill cow
- **Action**: Kill a cow directly
- **Expected**: Kill tracked in scoreboard

### 2. Permission System Tests

#### Test 2.1: Bypass Permission
- **Setup**: Give player `cowmurder.bypass` permission
- **Action**: Hit a cow
- **Expected**: 
  - Cow takes normal damage
  - No punishment applied
  - No scoreboard tracking

#### Test 2.2: Admin Commands
- **Setup**: Give player `cowmurder.admin` permission
- **Action**: Run `/cowmurder` command
- **Expected**: Shows help message with available subcommands

#### Test 2.3: No Permission
- **Setup**: Remove all cowmurder permissions from player
- **Action**: Try `/cowmurder` command
- **Expected**: "You don't have permission" message

### 3. Configuration Tests

#### Test 3.1: Punishment Types
Edit `config.yml` and test each punishment type:

**DEATH** (default):
- **Action**: Hit cow
- **Expected**: Player dies instantly

**DAMAGE**:
- **Config**: Set `punishment-type: DAMAGE` and `damage-amount: 5.0`
- **Action**: Hit cow with full health
- **Expected**: Player loses 5 hearts but doesn't die

**LIGHTNING_ONLY**:
- **Config**: Set `punishment-type: LIGHTNING_ONLY`
- **Action**: Hit cow
- **Expected**: Lightning effect only, no damage/death

#### Test 3.2: Lightning Effect Toggle
- **Config**: Set `lightning-effect: false`
- **Action**: Hit cow
- **Expected**: Punishment applied but no lightning

#### Test 3.3: Custom Death Messages
- **Config**: Modify `death-messages` list
- **Action**: Hit cow multiple times (respawn between)
- **Expected**: Different custom messages appear

#### Test 3.4: Disable Plugin
- **Config**: Set `enabled: false`
- **Action**: Restart server, hit cow
- **Expected**: Normal cow damage, no punishment

### 4. Scoreboard Tests

#### Test 4.1: Assault Tracking
- **Action**: Hit cows multiple times (die and respawn)
- **Verification**: Check scoreboard objectives or use `/cowmurder stats <player>`
- **Expected**: Assault count increases

#### Test 4.2: Kill Tracking  
- **Action**: Kill cows using creative mode or commands
- **Verification**: Check with `/cowmurder stats <player>`
- **Expected**: Kill count increases

#### Test 4.3: Disable Scoreboard
- **Config**: Set `scoreboard.enabled: false`
- **Action**: Restart server, hit cow
- **Expected**: No scoreboard tracking

### 5. Command Tests

#### Test 5.1: Reload Command
- **Action**: Modify config.yml, run `/cowmurder reload`
- **Expected**: "Configuration reloaded successfully" message
- **Verification**: Changes take effect without server restart

#### Test 5.2: Stats Command
- **Action**: `/cowmurder stats <playername>`
- **Expected**: Shows player's cow assault and kill counts

#### Test 5.3: Invalid Commands
- **Action**: `/cowmurder invalid`
- **Expected**: "Unknown subcommand" error message

### 6. Edge Case Tests

#### Test 6.1: Multiple Rapid Hits
- **Action**: Try to hit cow multiple times very quickly
- **Expected**: Only first hit registers, player dies once

#### Test 6.2: Different Cow Types
- **Action**: Test with baby cows, different cow variants
- **Expected**: All cow types protected equally

#### Test 6.3: Indirect Damage
- **Action**: Use TNT, lava, or other indirect methods near cows
- **Expected**: Only direct player damage triggers punishment

#### Test 6.4: Server Restart
- **Action**: Hit cows, check stats, restart server, check stats again
- **Expected**: Scoreboard data persists across restarts

### 7. Performance Tests

#### Test 7.1: Multiple Players
- **Setup**: Have multiple players hit cows simultaneously
- **Expected**: All players punished correctly, no conflicts

#### Test 7.2: Debug Mode
- **Config**: Set `debug: true`
- **Action**: Perform various actions
- **Verification**: Check server console for debug messages

## Expected Console Messages

### Normal Operation
```
[INFO] CowMurder has been enabled!
[INFO] Created assault scoreboard objective: cowAssaults
[INFO] Created kill scoreboard objective: cowKills
```

### Debug Mode (when enabled)
```
[INFO] Debug mode is enabled.
[INFO] Player PlayerName bypassed cow protection with permission.
[INFO] Tracked assault for PlayerName, new score: 1
[INFO] Applied death punishment to PlayerName
```

### Error Scenarios to Watch For
- Any `[WARNING]` or `[SEVERE]` messages in console
- Plugin failing to load
- Commands not working
- Scoreboard objectives not being created

## Post-Test Verification

1. **Check server performance**: Monitor for any lag or issues
2. **Review console logs**: Look for any warnings or errors
3. **Verify file integrity**: Config files should remain intact
4. **Test cleanup**: Restart server to ensure clean shutdown

## Bug Report Template

If you find issues, please note:
- **Minecraft Version**: 1.21.4
- **Server Type**: Spigot/Paper/etc.
- **Plugin Version**: 1.0.3
- **Steps to Reproduce**: Exact actions taken
- **Expected Behavior**: What should happen
- **Actual Behavior**: What actually happened
- **Console Output**: Any relevant error messages
- **Configuration**: Relevant config.yml settings

## Configuration Reference

Key settings to test with different values:
- `settings.punishment-type`: DEATH, DAMAGE, LIGHTNING_ONLY
- `settings.damage-amount`: Any positive number
- `settings.lightning-effect`: true/false
- `settings.custom-death-messages`: true/false
- `settings.enabled`: true/false
- `settings.debug`: true/false
- `scoreboard.enabled`: true/false
- `scoreboard.track-assaults`: true/false
- `scoreboard.track-kills`: true/false