package dev.club.aquatic;

import dev.club.aquatic.bstats.Metrics;
import dev.club.aquatic.commands.Executor;
import dev.club.aquatic.commands.tab.TabComplete;
import dev.club.aquatic.files.YML;
import dev.club.aquatic.listeners.DefaultListener;
import dev.club.aquatic.listeners.JoinListener;
import dev.club.aquatic.listeners.SpawnListener;
import dev.club.aquatic.manager.MenuManager;
import dev.club.aquatic.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Vasty
 * @date 13/12/2024 @ 23:26
 * @url https://github.com/vastydev
 */

public final class AquaSpawn extends JavaPlugin {

    private MenuManager menuManager;
    String pluginVersion = getDescription().getVersion();
    String bukkitVersion = Bukkit.getBukkitVersion();

    private static AquaSpawn instance;
    public static AquaSpawn getInstance() {
        return instance;
    }

    private static YML menu;
    public static YML SetMenu() {
        return menu;
    }

    private static YML config;
    public static YML SetConfig() {
        return config;
    }

    @Override
    public void onEnable() {
        instance = this;
        menu = new YML("menu");
        config = new YML("config");
        menuManager = new MenuManager(this);

        Bukkit.getConsoleSender().sendMessage(ColorUtils.Set("&r"));
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Set("&b    _   ___"));
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Set("&3   /_\\ / __|   &bAquaSpawn &f(v" + pluginVersion + "&f) - &aEnabled"));
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Set("&3  / _ \\__ \\    &fRunning on &7" + bukkitVersion));
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Set("&3 /_/ \\_\\___/"));
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Set("&r"));


        /**
         * @info Commands - executor.java
         */
        getCommand("aquaspawn").setExecutor(new Executor(this, menuManager));
        getCommand("aquaspawn").setTabCompleter(new TabComplete());

        /**
         * @info Event starter
         */
        Listeners();


        /**
         * @url bstats.org/plugin/bukkit/AquaSpawn/24161
         * @url bstats.org/plugin/bukkit/Aquatic%20Studios/24142
         */

        Metrics AquaSpawnBstats = new Metrics(this, 24161);
        Metrics AquaticStudiosBstats = new Metrics(this, 24142);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Set("&r"));
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Set("&c    _   ___"));
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Set("&c   /_\\ / __|   &bAquaSpawn &f(v" + pluginVersion + "&f) - &cDisabled"));
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Set("&c  / _ \\__ \\    &fRunning on &7" + bukkitVersion));
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Set("&c /_/ \\_\\___/"));
        Bukkit.getConsoleSender().sendMessage(ColorUtils.Set("&r"));
    }

    public void Listeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents((Listener) new SpawnListener(), (Plugin) this);
        pm.registerEvents((Listener) new JoinListener(), (Plugin) this);
        pm.registerEvents((Listener) new DefaultListener(), (Plugin) this);
    }

}