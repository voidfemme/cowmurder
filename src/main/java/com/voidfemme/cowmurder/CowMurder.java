package com.voidfemme.cowmurder;

import org.bukkit.Bukkit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.world.entity.projectile.Projectile;

import org.bukkit.scoreboard.Criteria;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CowMurder extends JavaPlugin implements Listener {
    private FileConfiguration config;
    private final Random random = new Random();
    private final Map<UUID, PendingPunishment> pendingPunishments = new ConcurrentHashMap<>();

    private static class PendingPunishment {
        final String deathMessage;
        final long timestamp;

        PendingPunishment(String deathMessage) {
            this.deathMessage = deathMessage;
            this.timestamp = System.currentTimeMillis();
        }
    }

    @Override
    public void onEnable() {
        try {
            saveDefaultConfig();
            config = getConfig();
            if (!config.getBoolean("settings.enabled", true)) {
                getLogger().info("CowMurder is disabled via configuration.");
                return;
            }
            getServer().getPluginManager().registerEvents(this, this);
            setupScoreboard();
            // Clean up expired pending punishments every 30 seconds
            new BukkitRunnable() {
                @Override
                public void run() {
                    cleanupExpiredPunishments();
                }
            }.runTaskTimer(this, 600L, 600L); // 30 seconds = 600 ticks
            getLogger().info("CowMurder has been enabled!");
            if (config.getBoolean("settings.debug", false)) {
                getLogger().info("Debug mode is enabled.");
            }
        } catch (Exception e) {
            getLogger().severe("Failed to enable CowMurder: " + e.getMessage());
            e.printStackTrace();
            setEnabled(false);
        }
    }

    @Override
    public void onDisable() {
        pendingPunishments.clear();
        getLogger().info("CowMurder has been disabled!");
    }

    private void setupScoreboard() {
        if (!config.getBoolean("scoreboard.enabled", true)) {
            return;
        }
        try {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            if (manager == null) {
                getLogger().warning("ScoreboardManager is null, cannot setup scoreboard.");
                return;
            }
            Scoreboard board = manager.getMainScoreboard();
            if (board == null) {
                getLogger().warning("Main scoreboard is null, cannot setup scoreboard.");
                return;
            }
            String assaultObjective = config.getString("scoreboard.assault-objective", "cowAssaults");
            String killObjective = config.getString("scoreboard.kill-objective", "cowKills");
            String assaultDisplay = config.getString("scoreboard.assault-display", "Cow Assaults");
            String killDisplay = config.getString("scoreboard.kill-display", "Cow Kills");
            if (config.getBoolean("scoreboard.track-assaults", true) && board.getObjective(assaultObjective) == null) {
                board.registerNewObjective(assaultObjective, Criteria.DUMMY, Component.text(assaultDisplay));
                if (config.getBoolean("settings.debug", false)) {
                    getLogger().info("Created assault scoreboard objective: " + assaultObjective);
                }
            }
            if (config.getBoolean("scoreboard.track-kills", true) && board.getObjective(killObjective) == null) {
                board.registerNewObjective(killObjective, Criteria.DUMMY, Component.text(killDisplay));
                if (config.getBoolean("settings.debug", false)) {
                    getLogger().info("Created kill scoreboard objective: " + killObjective);
                }
            }
        } catch (Exception e) {
            getLogger().warning("Failed to setup scoreboard: " + e.getMessage());
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!config.getBoolean("settings.enabled", true)) {
            getLogger().info("Plugin is disabled, returning");
            return;
        }

        // Check if the entity is a cow
        if (!(event.getEntity() instanceof Cow) || !(event.getDamager() instanceof Player player)) {
            return;
        }

        player = null;

        // Check if damager is a player (direct damage)
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        // Check if damager is a projectile shot by a player
        else if (event.getDamager() instanceof Projectile projectile) {
            if (projectile.getOwner() instanceof Player shooter) {
                player = shooter;
            }
        }

        // If no player found, return
        if (player == null) {
            getLogger().info("Damager is not a player or player-shot projectile");
            return;
        }

        try {
            // Check bypass permission
            String bypassPerm = config.getString("permissions.bypass-permission", "cowmurder.bypass");
            getLogger().info("Checking bypass permission: " + bypassPerm);
            if (player.hasPermission(bypassPerm)) {
                getLogger().info("Player " + player.getName() + " has bypass permission - allowing damage");
                if (config.getBoolean("settings.debug", false)) {
                    getLogger().info("Player " + player.getName() + " bypassed cow protection with permission.");
                }
                return;
            }

            // Cancel the original damage event
            event.setCancelled(true);
            // getLogger().info("Damage event cancelled");

            // Track assault in scoreboard
            // getLogger().info("Tracking assault...");
            trackAssault(player);

            // Apply punishment
            // getLogger().info("Applying punishment...");
            applyPunishment(player);

            // getLogger().info("=== COW PROTECTION COMPLETE ===");
        } catch (Exception e) {
            getLogger().warning("Error handling cow damage by " + player.getName() + ": " + e.getMessage());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!config.getBoolean("settings.enabled", true) || !config.getBoolean("scoreboard.track-kills", true)) {
            return;
        }
        if (!(event.getEntity() instanceof Cow) || !(event.getEntity().getKiller() instanceof Player player)) {
            return;
        }
        try {
            trackKill(player);
        } catch (Exception e) {
            getLogger().warning("Error tracking cow kill by " + player.getName() + ": " + e.getMessage());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PendingPunishment punishment = pendingPunishments.remove(player.getUniqueId());
        if (punishment != null && config.getBoolean("settings.custom-death-messages", true)) {
            event.deathMessage(Component.text(punishment.deathMessage));
            if (config.getBoolean("settings.debug", false)) {
                getLogger().info("Applied custom death message for " + player.getName());
            }
        }
    }

    private void trackAssault(Player player) {
        if (!config.getBoolean("scoreboard.enabled", true) || !config.getBoolean("scoreboard.track-assaults", true)) {
            return;
        }
        try {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            if (manager == null)
                return;
            Scoreboard board = manager.getMainScoreboard();
            if (board == null)
                return;
            String objectiveName = config.getString("scoreboard.assault-objective", "cowAssaults");
            Objective objective = board.getObjective(objectiveName);
            if (objective != null) {
                Score score = objective.getScore(player.getName());
                score.setScore(score.getScore() + 1);
                if (config.getBoolean("settings.debug", false)) {
                    getLogger().info("Tracked assault for " + player.getName() + ", new score: " + score.getScore());
                }
            }
        } catch (Exception e) {
            getLogger().warning("Failed to track assault for " + player.getName() + ": " + e.getMessage());
        }
    }

    private void trackKill(Player player) {
        try {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            if (manager == null)
                return;
            Scoreboard board = manager.getMainScoreboard();
            if (board == null)
                return;
            String objectiveName = config.getString("scoreboard.kill-objective", "cowKills");
            Objective objective = board.getObjective(objectiveName);
            if (objective != null) {
                Score score = objective.getScore(player.getName());
                score.setScore(score.getScore() + 1);
                if (config.getBoolean("settings.debug", false)) {
                    getLogger().info("Tracked kill for " + player.getName() + ", new score: " + score.getScore());
                }
            }
        } catch (Exception e) {
            getLogger().warning("Failed to track kill for " + player.getName() + ": " + e.getMessage());
        }
    }

    private void applyPunishment(Player player) {
        String punishmentType = config.getString("settings.punishment-type", "DEATH").toUpperCase();
        // Lightning effect
        if (config.getBoolean("settings.lightning-effect", true)) {
            try {
                player.getWorld().strikeLightningEffect(player.getLocation());
            } catch (Exception e) {
                getLogger().warning("Failed to create lightning effect: " + e.getMessage());
            }
        }
        // Prepare death message if needed
        String deathMessage = null;
        if (config.getBoolean("settings.custom-death-messages", true)) {
            deathMessage = getRandomDeathMessage(player.getName());
            pendingPunishments.put(player.getUniqueId(), new PendingPunishment(deathMessage));
        }
        // Apply punishment
        switch (punishmentType) {
            case "DEATH":
                try {
                    player.setHealth(0);
                    if (config.getBoolean("settings.debug", false)) {
                        getLogger().info("Applied death punishment to " + player.getName());
                    }
                } catch (Exception e) {
                    getLogger().warning("Failed to kill player " + player.getName() + ": " + e.getMessage());
                }
                break;
            case "DAMAGE":
                try {
                    double damage = config.getDouble("settings.damage-amount", 10.0);
                    double newHealth = Math.max(0, player.getHealth() - damage);
                    player.setHealth(newHealth);
                    if (config.getBoolean("settings.debug", false)) {
                        getLogger().info("Applied " + damage + " damage to " + player.getName());
                    }
                } catch (Exception e) {
                    getLogger().warning("Failed to damage player " + player.getName() + ": " + e.getMessage());
                }
                break;
            case "LIGHTNING_ONLY":
                // Lightning effect already applied above
                if (config.getBoolean("settings.debug", false)) {
                    getLogger().info("Applied lightning-only punishment to " + player.getName());
                }
                break;
            default:
                getLogger().warning("Unknown punishment type: " + punishmentType + ". Defaulting to DEATH.");
                try {
                    player.setHealth(0);
                } catch (Exception e) {
                    getLogger().warning("Failed to apply default death punishment: " + e.getMessage());
                }
                break;
        }
    }

    private String getRandomDeathMessage(String playerName) {
        List<String> messages = config.getStringList("death-messages");
        if (messages.isEmpty()) {
            return playerName + " was punished for harming a cow";
        }
        String message = messages.get(random.nextInt(messages.size()));
        return message.replace("%player%", playerName);
    }

    private void cleanupExpiredPunishments() {
        long currentTime = System.currentTimeMillis();
        long expireTime = 10000; // 10 seconds
        pendingPunishments.entrySet().removeIf(entry -> {
            boolean expired = (currentTime - entry.getValue().timestamp) > expireTime;
            if (expired && config.getBoolean("settings.debug", false)) {
                getLogger().info("Cleaned up expired punishment for player UUID: " + entry.getKey());
            }
            return expired;
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("cowmurder")) {
            return false;
        }
        String adminPerm = config.getString("permissions.admin-permission", "cowmurder.admin");
        if (!sender.hasPermission(adminPerm)) {
            sender.sendMessage(Component.text("You don't have permission to use this command.", NamedTextColor.RED));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§6CowMurder v" + getPluginMeta().getVersion());
            sender.sendMessage(Component.text("Usage: /cowmurder [reload|stats]", NamedTextColor.GOLD));
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "reload":
                try {
                    reloadConfig();
                    config = getConfig();
                    sender.sendMessage(
                            Component.text("CowMurder configuration reloaded successfully!", NamedTextColor.GREEN));
                } catch (Exception e) {
                    sender.sendMessage("§cFailed to reload configuration: " + e.getMessage());
                    getLogger().warning("Failed to reload config: " + e.getMessage());
                }
                break;
            case "stats":
                if (args.length > 1) {
                    showPlayerStats(sender, args[1]);
                } else {
                    sender.sendMessage(Component.text("Usage: /cowmurder stats <player>", NamedTextColor.GOLD));
                }
                break;
            default:
                sender.sendMessage(
                        Component.text("Unknown subcommand. Use: /cowmurder [reload|stats]", NamedTextColor.RED));
                break;
        }
        return true;
    }

    private void showPlayerStats(CommandSender sender, String playerName) {
        try {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            if (manager == null) {
                sender.sendMessage(Component.text("Scoreboard manager is not available.", NamedTextColor.RED));
                return;
            }
            Scoreboard board = manager.getMainScoreboard();
            if (board == null) {
                sender.sendMessage(Component.text("Main scoreboard is not available.", NamedTextColor.RED));
                return;
            }
            sender.sendMessage(Component.text("=== Cow Stats for " + playerName + " ===", NamedTextColor.GOLD));
            if (config.getBoolean("scoreboard.track-assaults", true)) {
                String assaultObj = config.getString("scoreboard.assault-objective", "cowAssaults");
                Objective assaults = board.getObjective(assaultObj);
                if (assaults != null) {
                    int assaultScore = assaults.getScore(playerName).getScore();
                    sender.sendMessage(Component.text("Cow Assaults: " + assaultScore, NamedTextColor.YELLOW));
                }
            }
            if (config.getBoolean("scoreboard.track-kills", true)) {
                String killObj = config.getString("scoreboard.kill-objective", "cowKills");
                Objective kills = board.getObjective(killObj);
                if (kills != null) {
                    int killScore = kills.getScore(playerName).getScore();
                    sender.sendMessage(Component.text("Cow Kills: " + killScore, NamedTextColor.YELLOW));
                }
            }
        } catch (Exception e) {
            sender.sendMessage("§cError retrieving stats: " + e.getMessage());
            getLogger().warning("Error showing stats for " + playerName + ": " + e.getMessage());
        }
    }
}
