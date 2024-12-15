package dev.club.aquatic.listeners;

import dev.club.aquatic.AquaSpawn;
import dev.club.aquatic.utils.ColorUtils;
import dev.club.aquatic.utils.Tools;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;

import java.util.List;

/**
 * @author Vasty
 * @date 14/12/2024 @ 02:21
 * @url https://github.com/vastydev
 */

public class JoinListener implements Listener {

    private String setPlaceholders(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!AquaSpawn.SetConfig().getBoolean("join.switch")) return;

        List<String> welcomeMessages = AquaSpawn.SetConfig().getStringList("join.message");
        if (welcomeMessages == null || welcomeMessages.isEmpty()) return;

        for (String rawMessage : welcomeMessages) {
            String message = rawMessage.trim();

            if (message.equalsIgnoreCase("<empty>")) {
                message = "&r";
            }

            if (AquaSpawn.getInstance().getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                message = setPlaceholders(player, message);
            }

            if (message.contains("<center>")) {
                message = Tools.CenterMessage(message.replace("<center>", ""));
            }

            player.sendMessage(ColorUtils.Set(message));
        }
    }
}