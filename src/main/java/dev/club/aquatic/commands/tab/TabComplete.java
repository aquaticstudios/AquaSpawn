package dev.club.aquatic.commands.tab;

import dev.club.aquatic.AquaSpawn;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vasty
 * @date 13/12/2024 @ 23:26
 * @url https://github.com/vastydev
 */

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("aquaspawn.admin")) {
                completions.add("help");
                completions.add("create");
                completions.add("set");
                completions.add("menu");
                completions.add("reload");
            }
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            if (sender.hasPermission("aquaspawn.admin")) {
                ConfigurationSection items = AquaSpawn.SetMenu().getConfigurationSection("items");
                if (items != null) {
                    for (String key : items.getKeys(false)) {
                        if (!key.equalsIgnoreCase("close")) {
                            completions.add(key);
                        }
                    }
                }
            }
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
            if (sender.hasPermission("aquaspawn.admin")) {
                completions.add("force");
                completions.add("first");
            }
        }
        return completions;
    }
}
