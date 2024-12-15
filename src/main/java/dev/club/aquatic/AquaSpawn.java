package dev.club.aquatic;

import dev.club.aquatic.bstats.Metrics;
import dev.club.aquatic.commands.Executor;
import dev.club.aquatic.commands.tab.TabComplete;
import dev.club.aquatic.files.YML;
import dev.club.aquatic.listeners.DefaultListener;
import dev.club.aquatic.listeners.JoinListener;
import dev.club.aquatic.listeners.SpawnListener;
import dev.club.aquatic.manager.MenuManager;
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
         * @url bstats.org/plugin/bukkit/Aquatic%20Studios/24142
         */
        int pluginId = 24142;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        getLogger().info("AquaSpawn plugin disabled!");
    }

    public void Listeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents((Listener) new SpawnListener(), (Plugin) this);
        pm.registerEvents((Listener) new JoinListener(), (Plugin) this);
        pm.registerEvents((Listener) new DefaultListener(), (Plugin) this);
    }

}