package dev.club.aquatic.manager;

import java.util.ArrayList;
import java.util.List;

import com.cryptomorin.xseries.XMaterial;
import dev.club.aquatic.AquaSpawn;
import dev.club.aquatic.handler.ActionHandler;
import dev.club.aquatic.listeners.menu.MenuListener;
import dev.club.aquatic.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import me.clip.placeholderapi.PlaceholderAPI;

/**
 * @author Vasty
 * @date 13/12/2024 @ 23:26
 * @url https://github.com/vastydev
 */

public class MenuManager {
    private Inventory menu;
    private ActionHandler actionHandler = new ActionHandler();

    public MenuManager(JavaPlugin plugin) {
        ConfigurationSection menuConfig = AquaSpawn.SetMenu().getConfigurationSection("menu");
        if (menuConfig == null) {
            plugin.getLogger().severe("Menu configuration section is missing!");
            return;
        }

        String title = menuConfig.getString("title", "Menu");
        int size = menuConfig.getInt("size", 6) * 9;
        this.menu = Bukkit.createInventory(null, size, ColorUtils.Set(title));

        ConfigurationSection itemsSection = AquaSpawn.SetMenu().getConfigurationSection("items");
        if (itemsSection != null) {
            loadItemsFromConfig(itemsSection);
        } else {
            plugin.getLogger().severe("Items configuration section is missing!");
        }

        Bukkit.getPluginManager().registerEvents(new MenuListener(this), plugin);
    }

    public void openMenu(Player player) {
        player.openInventory(this.menu);
    }

    public void loadItemsFromConfig(ConfigurationSection itemsSection) {
        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection itemData = itemsSection.getConfigurationSection(key);
            String name = itemData.getString("name", "Unnamed");
            String materialData = itemData.getString("material", "STONE");
            int slot = itemData.getInt("slot", 0);
            List<String> lore = itemData.getStringList("lore");
            List<String> actions = itemData.getStringList("multi-click");
            name = ColorUtils.Set(name);
            registerItem(name, materialData, slot, lore, actions);
        }
    }

    private void registerItem(String name, String materialData, int slot, List<String> lore, List<String> actions) {
        Material material = ((XMaterial)XMaterial.matchXMaterial(materialData).orElse(XMaterial.STONE)).parseMaterial();
        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ColorUtils.Set(name));
            List<String> coloredLore = new ArrayList<>();
            for (String line : lore) {
                String processedLine = ColorUtils.Set(PlaceholderAPI.setPlaceholders(null, line));
                coloredLore.add(processedLine);
            }
            meta.setLore(coloredLore);

            item.setItemMeta(meta);
        }

        this.menu.setItem(slot, item);
        this.actionHandler.registerItemActions(slot, actions);
    }

    public void handleInventoryClick(InventoryClickEvent event) {
        final Player player = (Player)event.getWhoClicked();
        int slot = event.getSlot();
        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(this.menu))
            return;
        event.setCancelled(true);
        List<String> actions = this.actionHandler.getActions(slot);
        if (actions != null)
            for (String action : actions) {
                (new BukkitRunnable() {
                    public void run() {
                        MenuManager.this.actionHandler.handleAction(player, action);
                    }
                }).runTaskLater((Plugin)JavaPlugin.getProvidingPlugin(getClass()), 0L);
            }
    }
}
