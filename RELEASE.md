# GitHub Release Instructions for CowMurder

## Prerequisites

- Your code is committed and pushed to the main branch
- You have completed playtesting (see PLAYTEST.md)
- The plugin builds successfully
- You have maintainer access to the GitHub repository

## Step-by-Step Release Process

### Step 1: Update Version Number

1. **Update `build.gradle`**:
   ```gradle
   version = '1.1.0'  // Change from '1.0.3' to new version
   ```

2. **Commit the version change**:
   ```bash
   git add build.gradle
   git commit -m "Bump version to 1.1.0"
   git push origin main
   ```

### Step 2: Build the Release JAR

1. **Clean and build**:
   ```bash
   ./gradlew clean build
   ```

2. **Locate the built JAR**:
   - Find: `build/libs/CowMurder.jar` (shadowJar output)
   - This is your release artifact

### Step 3: Create Git Tag

1. **Create and push a tag**:
   ```bash
   git tag v1.1.0
   git push origin v1.1.0
   ```

### Step 4: Create GitHub Release

#### Option A: Using GitHub Web Interface

1. **Navigate to your repository** on GitHub
2. **Click "Releases"** (right sidebar or under "Code" tab)
3. **Click "Create a new release"**
4. **Fill out the release form**:

   **Tag version**: `v1.1.0` (should auto-populate if you pushed the tag)
   
   **Release title**: `CowMurder v1.1.0`
   
   **Description** (example):
   ```markdown
   ## What's New in v1.1.0
   
   ### 🚀 Major Features
   - **Configurable Settings**: Full `config.yml` with customizable punishment types, messages, and behavior
   - **Permission System**: Bypass permissions for admins and command permissions
   - **Admin Commands**: `/cowmurder reload` and `/cowmurder stats <player>`
   - **Enhanced Logging**: Debug mode and comprehensive error handling
   
   ### 🐛 Bug Fixes
   - Fixed memory leak from temporary event listeners
   - Added proper null checks and error handling
   - Improved scoreboard management
   
   ### ⚙️ Technical Improvements
   - Updated for Minecraft 1.21.4 compatibility
   - Better code maintainability and structure
   - Comprehensive configuration options
   
   ### 📋 Configuration Options
   - **Punishment Types**: Choose between instant death, damage, or lightning-only
   - **Custom Death Messages**: Fully customizable with player placeholders
   - **Scoreboard Control**: Enable/disable tracking for assaults and kills
   - **Permission Integration**: Bypass protection with permissions
   
   ## Installation
   
   1. Download `CowMurder.jar` below
   2. Place in your server's `plugins` folder
   3. Restart your server
   4. Configure `plugins/CowMurder/config.yml` as needed
   5. Use `/cowmurder reload` to apply config changes
   
   ## Compatibility
   
   - **Minecraft Version**: 1.21.4
   - **Server Software**: Spigot, Paper, and derivatives
   - **Java Version**: 21 or higher
   
   ## Commands & Permissions
   
   - `/cowmurder reload` - Reload configuration (requires `cowmurder.admin`)
   - `/cowmurder stats <player>` - View player statistics (requires `cowmurder.admin`)
   - `cowmurder.bypass` - Bypass cow protection
   - `cowmurder.admin` - Access admin commands
   
   ---
   
   **Full Changelog**: https://github.com/yourusername/cow_murder/compare/v1.0.3...v1.1.0
   ```

5. **Attach the JAR file**:
   - Drag and drop `build/libs/CowMurder.jar` into the "Attach binaries" section
   - Or click "Attach binaries" and select the file

6. **Set release options**:
   - ☐ Set as pre-release (only if this is a beta/alpha)
   - ☐ Set as latest release (check this for main releases)

7. **Click "Publish release"**

#### Option B: Using GitHub CLI (if you have `gh` installed)

```bash
# Create release with JAR attachment
gh release create v1.1.0 build/libs/CowMurder.jar \
  --title "CowMurder v1.1.0" \
  --notes-file release-notes.md
```

### Step 5: Post-Release Tasks

1. **Update README.md** if needed:
   - Update installation instructions with new version number
   - Update compatibility information
   - Add any new features to the feature list

2. **Update download links** in documentation to point to latest release

3. **Announce the release** (if applicable):
   - Discord servers
   - Forums
   - Social media

## Version Numbering Guidelines

Use [Semantic Versioning](https://semver.org/):
- **MAJOR** (X.0.0): Breaking changes or major rewrites
- **MINOR** (1.X.0): New features, backward compatible
- **PATCH** (1.1.X): Bug fixes, backward compatible

### Examples:
- `1.0.3` → `1.1.0`: Added configuration system and commands (minor)
- `1.1.0` → `1.1.1`: Fixed a bug (patch)
- `1.1.1` → `2.0.0`: Rewrote core functionality (major)

## Release Notes Template

```markdown
## What's New in vX.X.X

### 🚀 New Features
- List new features here

### 🐛 Bug Fixes
- List bug fixes here

### ⚙️ Changes
- List other changes here

### 🔧 Technical
- List technical improvements here

## Installation
[Standard installation instructions]

## Compatibility
- **Minecraft Version**: X.XX.X
- **Server Software**: Spigot, Paper
- **Java Version**: XX or higher

## Breaking Changes (if any)
- List any breaking changes that require user action
```

## Troubleshooting

### "Tag already exists"
```bash
# Delete local tag
git tag -d v1.1.0
# Delete remote tag
git push origin --delete v1.1.0
# Create new tag
git tag v1.1.0
git push origin v1.1.0
```

### "JAR not found"
- Make sure you ran `./gradlew build` successfully
- Check `build/libs/` directory exists and contains the JAR
- Verify the JAR filename matches what you're trying to upload

### "Permission denied"
- Ensure you have write access to the repository
- Check if branch protection rules are blocking the release
- Make sure you're authenticated with GitHub

## Quick Checklist

- [ ] Code is tested and working
- [ ] Version number updated in `build.gradle`
- [ ] Changes committed and pushed to main branch
- [ ] JAR built successfully with `./gradlew build`
- [ ] Git tag created and pushed
- [ ] GitHub release created with proper title and description
- [ ] JAR file attached to release
- [ ] Release published
- [ ] Documentation updated if needed

## Next Steps After Release

1. Monitor for any issues reported by users
2. Update project management tools if you use them
3. Plan next version features based on user feedback
4. Consider creating a roadmap for future releases