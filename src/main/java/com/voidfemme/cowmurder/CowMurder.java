package com.voidfemme.cowmurder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.damage.DamageSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CowMurder extends JavaPlugin implements Listener {
    private static final String[] DEATH_MESSAGES = {
        "%s was moo-rdered for their bovine crimes",
        "%s faced divine bovine retribution",
        "The cows fought back, and %s lost",
        "%s learned the hard way not to mess with cows",
        "A mysterious force struck down %s for harming a cow"
    };

    private Random random = new Random();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        setupScoreboard();
        getLogger().info("CowMurder has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("CowMurder has been disabled!");
    }

    private void setupScoreboard() {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        if (board.getObjective("cowAssaults") == null) {
            board.registerNewObjective("cowAssaults", "dummy", "Cow Assaults");
        }
        if (board.getObjective("cowKills") == null) {
            board.registerNewObjective("cowKills", "dummy", "Cow Kills");
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Cow cow && event.getDamager() instanceof Player player) {
            Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
            Objective assaultObjective = board.getObjective("cowAssaults");
            Score assaultScore = assaultObjective.getScore(player.getName());
            assaultScore.setScore(assaultScore.getScore() + 1);
    
            // Cancel the original damage event
            event.setCancelled(true);
    
            // Kill the player
            player.setHealth(0);
    
            // Summon lightning effect
            player.getWorld().strikeLightningEffect(player.getLocation());
            
            // Get the random death message
            String deathMessage = getRandomDeathMessage(player.getName());
    
            // Register a temporary listener to set the death message
            Bukkit.getPluginManager().registerEvents(new Listener() {
                @EventHandler(priority = EventPriority.HIGHEST)
                public void onPlayerDeath(PlayerDeathEvent event) {
                    if (event.getEntity().equals(player)) {
                        event.setDeathMessage(deathMessage);
                        // Unregister this listener after it's used
                        HandlerList.unregisterAll(this);
                    }
                }
            }, this);
        }
    }    

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Cow && event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity().getKiller();
            
            Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
            Objective killObjective = board.getObjective("cowKills");
            Score killScore = killObjective.getScore(player.getName());
            killScore.setScore(killScore.getScore() + 1);
        }
    }

    private String getRandomDeathMessage(String playerName) {
        return String.format(DEATH_MESSAGES[random.nextInt(DEATH_MESSAGES.length)], playerName);
    }
}
