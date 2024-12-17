package dev.club.aquatic.commands;

import dev.club.aquatic.AquaSpawn;
import dev.club.aquatic.manager.MenuManager;
import dev.club.aquatic.utils.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vasty
 * @date 13/12/2024 @ 23:26
 * @url https://github.com/vastydev
 */

public class Executor implements CommandExecutor {

    String dateUpdate = "15/12";
    String pluginVersion = AquaSpawn.getInstance().getDescription().getVersion();

    private final AquaSpawn plugin;
    private MenuManager menuManager;

    public Executor(AquaSpawn plugin, MenuManager menuManager) {
        this.plugin = plugin;
        this.menuManager = menuManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ColorUtils.Set("&r"));
            sender.sendMessage(ColorUtils.Set("&r                     &#64ADFF&lAquaSpawn"));
            sender.sendMessage(ColorUtils.Set("&r                   &7Update @ " + dateUpdate));
            sender.sendMessage(ColorUtils.Set("&r"));
            sender.sendMessage(ColorUtils.Set("&r                    &#5FC3FCℹ Version: &f" + pluginVersion));
            sender.sendMessage(ColorUtils.Set("&r                   &#5FC3FC\uD83D\uDC51 Author: &fVasty"));
            sender.sendMessage(ColorUtils.Set("&r"));
            sender.sendMessage(ColorUtils.Set("&r           &#5FC3FCPowered by Aquatic Studios"));
            sender.sendMessage(ColorUtils.Set("&r"));
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            if (sender.hasPermission("aquaspawn.admin") || sender.hasPermission("aquaspawn.help")) {
                sender.sendMessage(ColorUtils.Set("&r"));
                sender.sendMessage(ColorUtils.Set("&r                     &#64ADFF&lAquaSpawn"));
                sender.sendMessage(ColorUtils.Set("&r                  &7Commands @ Vasty"));
                sender.sendMessage(ColorUtils.Set("&r"));
                sender.sendMessage(ColorUtils.Set("&r                   &#97D9FF/aquaspawn menu"));
                sender.sendMessage(ColorUtils.Set("&r                 &#97D9FF/aquaspawn reload"));
                sender.sendMessage(ColorUtils.Set("&r            &#97D9FF/aquaspawn create &f<name>"));
                sender.sendMessage(ColorUtils.Set("&r         &#97D9FF/aquaspawn set &f<name> <type>"));
                sender.sendMessage(ColorUtils.Set("&r"));
                sender.sendMessage(ColorUtils.Set("&r           &#5FC3FCPowered by Aquatic Studios"));
                sender.sendMessage(ColorUtils.Set("&r"));
            } else {
                sender.sendMessage(ColorUtils.Set(AquaSpawn.SetConfig().getString("messages.no-permission")));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (!sender.hasPermission("aquaspawn.admin") && !sender.hasPermission("aquaspawn.set")) {
                sender.sendMessage(ColorUtils.Set(AquaSpawn.SetConfig().getString("messages.no-permission")));
                return true;
            }

            if (args.length < 3) {
                sender.sendMessage(ColorUtils.Set(AquaSpawn.SetConfig().getString("messages.error-set-spawn")));
                return true;
            }

            String worldName = args[1];
            String type = args[2].toLowerCase();

            if (!type.equals("force") && !type.equals("first")) {
                sender.sendMessage(ColorUtils.Set(AquaSpawn.SetConfig().getString("messages.spawn-type-invalid")));
                return true;
            }

            AquaSpawn.SetConfig().set("settings.join-spawn", worldName);
            AquaSpawn.SetConfig().set("settings.type-spawn", type);
            AquaSpawn.SetConfig().Save();
            AquaSpawn.SetConfig().Reload();

            sender.sendMessage(ColorUtils.Set(AquaSpawn.SetConfig().getString("messages.set-spawn").replace("%aquaspawn_name%", worldName).replace("%aquaspawn_type%", type)));

            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (!sender.hasPermission("aquaspawn.admin") && !sender.hasPermission("aquaspawn.create")) {
                sender.sendMessage(ColorUtils.Set(AquaSpawn.SetConfig().getString("messages.no-permission")));
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage(ColorUtils.Set("&cThis command can only be used by players"));
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage(ColorUtils.Set(AquaSpawn.SetConfig().getString("messages.how-to-create")));
                return true;
            }

            Player player = (Player) sender;
            ConfigurationSection items = AquaSpawn.SetMenu().getConfigurationSection("items");
            int spawnNumber = 1;
            if (items != null) {
                spawnNumber = (int) items.getKeys(false).stream()
                        .filter(key -> !key.equalsIgnoreCase("close"))
                        .count() + 1;
            }

            String spawnName = args[1];
            String spawnNumberText = "&#5E9DFFSpawn #" + spawnNumber;
            String creator = player.getName();
            String worldName = player.getLocation().getWorld().getName();
            String coordinates = player.getLocation().getBlockX() + ", " + player.getLocation().getBlockZ();
            String teleport = player.getLocation().getX() + "," +
                    player.getLocation().getY() + "," +
                    player.getLocation().getZ() + "," +
                    player.getLocation().getYaw() + "," +
                    player.getLocation().getPitch();

            if (items != null && items.getKeys(false).stream()
                    .anyMatch(key -> key.equalsIgnoreCase(spawnName))) {
                sender.sendMessage(ColorUtils.Set(AquaSpawn.SetConfig().getString("messages.already-spawn").replace("%aquaspawn_name%", spawnName)));
                return true;
            }


            AquaSpawn.SetMenu().set("items." + spawnName + ".name", spawnNumberText);
            AquaSpawn.SetMenu().set("items." + spawnName + ".world", worldName);
            AquaSpawn.SetMenu().set("items." + spawnName + ".slot", getNextAvailableSlot(items));
            AquaSpawn.SetMenu().set("items." + spawnName + ".material", "MAP");
            AquaSpawn.SetMenu().set("items." + spawnName + ".cord", teleport);

            List<String> lore = new ArrayList<>();
            lore.add("&r");
            lore.add("&8        ℹ ɪɴꜰᴏʀᴍᴀᴛɪᴏɴ ℹ");
            lore.add("&r");
            lore.add("   &6⭐ &fName: &6" + spawnName);
            lore.add("   &e☄ &fCreated by: &e" + creator);
            lore.add("&r");
            lore.add("   &#BAFF89\uD83C\uDF0E &fWorld: &#BAFF89" + worldName);
            lore.add("   &#FFA7EF☀ &fCoordinates: &#FFA7EF" + coordinates);
            lore.add("&r");
            lore.add("    &#5E9DFF ⏏ ᴄʟɪᴄᴋ ᴛᴏ ᴛᴇʟᴇᴘᴏʀᴛ ⏏   ");

            AquaSpawn.SetMenu().set("items." + spawnName + ".lore", lore);

            List<String> actions = new ArrayList<>();
            actions.add("[SOUND] BLOCK_LAVA_POP:5:5");
            actions.add("[TELEPORT] " + teleport);
            actions.add("[CLOSE]");

            AquaSpawn.SetMenu().set("items." + spawnName + ".multi-click", actions);
            AquaSpawn.SetMenu().Save();
            AquaSpawn.SetMenu().Reload();
            this.menuManager = new MenuManager(plugin);

            sender.sendMessage(ColorUtils.Set(AquaSpawn.SetConfig().getString("messages.spawn-create").replace("%aquaspawn_name%", spawnName)));
            return true;
        }

        if (args[0].equalsIgnoreCase("menu")) {
            if (sender.hasPermission("aquaspawn.admin") || sender.hasPermission("aquaspawn.menu")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    menuManager.openMenu(player);
                } else {
                    sender.sendMessage(ColorUtils.Set("&cThis command can only be used by players"));
                }
                return true;
            }
            sender.sendMessage(ColorUtils.Set(AquaSpawn.SetConfig().getString("messages.no-permission")));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("aquaspawn.admin") || sender.hasPermission("aquaspawn.reload")) {
                AquaSpawn.SetMenu().Reload();
                AquaSpawn.SetConfig().Reload();
                this.menuManager = new MenuManager(plugin);
                sender.sendMessage(ColorUtils.Set(AquaSpawn.SetConfig().getString("messages.reload")));
                return true;
            } else {
                sender.sendMessage(ColorUtils.Set(AquaSpawn.SetConfig().getString("messages.no-permission")));
                return true;
            }
        }
        return false;
    }

    private int getNextAvailableSlot(ConfigurationSection items) {
        if (items == null) return 11;

        List<Integer> validSlots = new ArrayList<>();
        for (int row = 1; row <= 5; row++) {
            int startSlot = 9 * row + 2;
            for (int i = 0; i < 5; i++) {
                validSlots.add(startSlot + i);
            }
        }

        List<Integer> occupiedSlots = new ArrayList<>();
        items.getValues(false).values().stream()
                .filter(value -> value instanceof ConfigurationSection)
                .map(value -> (ConfigurationSection) value)
                .forEach(section -> occupiedSlots.add(section.getInt("slot")));

        for (int slot : validSlots) {
            if (!occupiedSlots.contains(slot)) {
                return slot;
            }
        }

        return -1;
    }
}