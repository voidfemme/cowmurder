package com.void_f3mme.cowmurder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Random;

public class CowMurder extends JavaPlugin implements Listener {
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
        if (event.getEntity() instanceof Cow && event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            
            Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
            Objective assaultObjective = board.getObjective("cowAssaults");
            Score assaultScore = assaultObjective.getScore(player.getName());
            assaultScore.setScore(assaultScore.getScore() + 1);

            player.setHealth(0);

            // Summon lightning
            player.getWorld().strikeLightningEffect(player.getLocation());
            
            String deathMessage = getRandomDeathMessage(player.getName());
            Bukkit.broadcastMessage(deathMessage);

            event.setCancelled(true);
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
        String[] messages = {
            playerName + " was moo-rdered for their bovine crimes",
            playerName + " faced divine bovine retribution",
            "The cows fought back, and " + playerName + " lost",
            playerName + " learned the hard way not to mess with cows",
            "A mysterious force struck down " + playerName + " for harming a cow"
        };
        return messages[random.nextInt(messages.length)];
    }
}
