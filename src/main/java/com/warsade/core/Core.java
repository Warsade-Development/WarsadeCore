package com.warsade.core;

import com.warsade.core.bukkit.commands.CoreCommand;
import com.warsade.core.bukkit.events.CustomMenusCommandListener;
import com.warsade.core.bukkit.view.CustomMenu;
import com.warsade.core.bukkit.view.PluginsMenu;
import com.warsade.core.config.Config;
import com.warsade.core.config.providers.MenuConfig;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class Core extends CorePlugin {

    Config settings = new Config(this, "settings");

    @Override
    public void onEnable() {
        super.onEnable();

        setPluginIcon(Material.BEDROCK);
        registerMenus(new PluginsMenu(new MenuConfig(settings, "plugins")));
        registerMenus(new CustomMenu(new MenuConfig("null", "custom menu", 1), this));

        getLogger().info("Core plugin enabled");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getLogger().info("Core plugin disabled");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        settings.reload();
    }

    @Override
    public void loadConfigs() {
        try {
            settings.load();
        } catch (IOException | InvalidConfigurationException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void registerListeners() {
        super.registerListeners();
        getServer().getPluginManager().registerEvents(new CustomMenusCommandListener(this), this);
    }

    @Override
    public void registerCommands() {
        super.registerCommands();
        getCommand("core").setExecutor(new CoreCommand(this));
    }

    public Config getSettings() {
        return settings;
    }

}
