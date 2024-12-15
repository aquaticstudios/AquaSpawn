package dev.club.aquatic.handler;

import com.cryptomorin.xseries.XSound;
import dev.club.aquatic.utils.ColorUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * @author Vasty
 * @date 13/12/2024 @ 23:26
 * @url https://github.com/vastydev
 */

public class ActionHandler {
    private final Map<Integer, List<String>> slotActions = new HashMap<>();

    private final Map<String, BiConsumer<Player, String>> actionMap = new HashMap<>();

    public ActionHandler() {
        registerAction("COMMAND", this::executeCommand);
        registerAction("MESSAGE", this::sendMessage);
        registerAction("SOUND", this::playSound);
        registerAction("TELEPORT", this::teleport);
        registerAction("CLOSE", this::closeMenu);
    }

    public void handleAction(Player player, String action) {
        String[] parts = action.split(" ", 2);
        String actionType = parts[0].replace("[", "").replace("]", "");
        String actionData = (parts.length > 1) ? parts[1] : "";
        BiConsumer<Player, String> executor = this.actionMap.get(actionType);
        if (executor != null) {
            executor.accept(player, actionData);
        } else {
            Bukkit.getLogger().warning("Unknown action type: " + actionType);
        }
    }

    public void registerAction(String actionType, BiConsumer<Player, String> executor) {
        this.actionMap.put(actionType, executor);
    }

    public void registerItemActions(int slot, List<String> actions) {
        this.slotActions.put(Integer.valueOf(slot), actions);
    }

    public List<String> getActions(int slot) {
        return this.slotActions.get(Integer.valueOf(slot));
    }

    private void closeMenu(Player player, String unused) {
        Inventory openInventory = player.getOpenInventory().getTopInventory();
        if (openInventory != null) {
            player.closeInventory();
        }
    }

    private void teleport(Player player, String teleportData) {
        try {
            String[] parts = teleportData.split(",");
            if (parts.length != 5) {
                player.sendMessage(ColorUtils.Set("&cInvalid teleportation data"));
                return;
            }

            double x = Double.parseDouble(parts[0]);
            double y = Double.parseDouble(parts[1]);
            double z = Double.parseDouble(parts[2]);
            float yaw = Float.parseFloat(parts[3]);
            float pitch = Float.parseFloat(parts[4]);

            if (player.getWorld() == null) {
                player.sendMessage(ColorUtils.Set("&cThe world cannot be determined for teleportation"));
                return;
            }

            Location targetLocation = new Location(player.getWorld(), x, y, z, yaw, pitch);
            player.teleport(targetLocation);
        } catch (NumberFormatException e) {
            player.sendMessage(ColorUtils.Set("&cInvalid coordinate format for teleportation"));
        }
    }


    private void executeCommand(Player player, String command) {
        String processedCommand = PlaceholderAPI.setPlaceholders(player, command).replace("%aquaspawn_player%", player.getName());
        Bukkit.dispatchCommand(player, processedCommand);
    }

    private void sendMessage(Player player, String message) {
        String processedMessage = PlaceholderAPI.setPlaceholders(player, message);
        player.sendMessage(ColorUtils.Set(processedMessage));
    }

    private void playSound(Player player, String soundData) {
        String[] soundParts = soundData.split(":");
        Sound sound = ((XSound)XSound.matchXSound(soundParts[0]).orElse(XSound.ENTITY_PLAYER_LEVELUP)).parseSound();
        float volume = (soundParts.length > 1) ? Float.parseFloat(soundParts[1]) : 1.0F;
        float pitch = (soundParts.length > 2) ? Float.parseFloat(soundParts[2]) : 1.0F;
        player.playSound(player.getLocation(), sound, volume, pitch);
    }
}
