package dev.club.aquatic.listeners.menu;

import dev.club.aquatic.manager.MenuManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author Vasty
 * @date 13/12/2024 @ 23:26
 * @url https://github.com/vastydev
 */

public class MenuListener implements Listener {
    private final MenuManager menuManager;

    public MenuListener(MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        this.menuManager.handleInventoryClick(event);
    }
}
