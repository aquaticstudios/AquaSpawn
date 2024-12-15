package dev.club.aquatic.listeners;

import dev.club.aquatic.AquaSpawn;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

/**
 * @author Vasty
 * @date 14/12/2024 @ 03:08
 * @url https://github.com/vastydev
 */

public class DefaultListener implements Listener {

    @EventHandler
    public void JoinUser(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (AquaSpawn.SetConfig().getBoolean("DefaultUserJoin")) {
            if (p.isOp()) {
                e.setJoinMessage(null);
            } else if (p.hasPermission("player.join.default")) {
                e.setJoinMessage(null);
            } else {
                e.setJoinMessage(null);
            }
        } else {
            e.setJoinMessage(null);
        }
    }

    @EventHandler
    public void QuitUser(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (AquaSpawn.SetConfig().getBoolean("DefaultUserLeave")) {
            if (p.isOp()) {
                e.setQuitMessage(null);
            } else if (p.hasPermission("players.user.leave")) {
                e.setQuitMessage(null);
            } else {
                e.setQuitMessage(null);
            }
        } else {
            e.setQuitMessage(null);
        }
    }

}
