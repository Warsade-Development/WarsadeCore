package com.warsade.core.bukkit.events;

import com.warsade.core.Core;
import com.warsade.core.bukkit.view.CustomMenu;
import com.warsade.core.config.providers.MenuConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CustomMenusCommandListener implements Listener {

    final private Core plugin;

    public CustomMenusCommandListener(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        FileConfiguration settings = plugin.getSettings().get();
        String command = event.getMessage().replaceFirst("/", "").toLowerCase();
        MenuConfig menuConfig = findCustomMenuByCommand(command, settings);

        if (menuConfig != null) new CustomMenu().openInventory(event.getPlayer(), menuConfig);
    }

    private MenuConfig findCustomMenuByCommand(String command, FileConfiguration settings) {
        ConfigurationSection customMenusSection = settings.getConfigurationSection("CustomMenus");
        if (customMenusSection != null) {
            for (String customMenuKey : customMenusSection.getKeys(false)) {
                String menuPath = "CustomMenus." + customMenuKey;
                if (settings.getString(menuPath + ".command", "").equals(command)) {
                    return new MenuConfig(settings, menuPath);
                }
            }
        }

        return null;
    }

}
