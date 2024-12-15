package dev.club.aquatic.listeners;

import dev.club.aquatic.AquaSpawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author Vasty
 * @date 13/12/2024 @ 23:26
 * @url https://github.com/vastydev
 */

public class SpawnListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String joinSpawnName = AquaSpawn.SetConfig().getString("settings.join-spawn");
        if (joinSpawnName == null || joinSpawnName.isEmpty()) {
            Bukkit.getLogger().warning("'join-spawn' not set in config.yml");
            return;
        }
        String typeSpawn = AquaSpawn.SetConfig().getString("settings.type-spawn");
        if (typeSpawn == null) {
            typeSpawn = "force";
        }

        if (typeSpawn.equalsIgnoreCase("first") && player.hasPlayedBefore()) {
            return;
        }

        String path = "items." + joinSpawnName;
        String worldName = AquaSpawn.SetMenu().getString(path + ".world");
        String cordString = AquaSpawn.SetMenu().getString(path + ".cord");

        if (worldName == null || cordString == null) {
            Bukkit.getLogger().warning("No data found for spawn '" + joinSpawnName + "' in config.yml");
            return;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            Bukkit.getLogger().warning("The world '" + worldName + "' does not exist or is not loaded");
            return;
        }

        String[] cords = cordString.split(",");
        if (cords.length < 3) {
            Bukkit.getLogger().warning("The coordinates for '" + joinSpawnName + "' are invalid in config.yml");
            return;
        }

        try {
            double x = Double.parseDouble(cords[0]);
            double y = Double.parseDouble(cords[1]);
            double z = Double.parseDouble(cords[2]);
            float yaw = cords.length > 3 ? Float.parseFloat(cords[3]) : 0f;
            float pitch = cords.length > 4 ? Float.parseFloat(cords[4]) : 0f;

            Location spawnLocation = new Location(world, x, y, z, yaw, pitch);

            player.teleport(spawnLocation);
            if (typeSpawn.equalsIgnoreCase("first")) {
            }

        } catch (NumberFormatException e) {
            Bukkit.getLogger().warning("Error parsing coordinates for '" + joinSpawnName + "' in config.yml");
        } catch (Exception e) {
            Bukkit.getLogger().warning("An error occurred while trying to teleport the player: " + e.getMessage());
        }
    }
}