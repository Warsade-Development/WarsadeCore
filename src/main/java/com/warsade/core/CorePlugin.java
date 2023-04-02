package com.warsade.core;

import com.warsade.core.menu.Menu;
import com.warsade.core.menu.registry.MenuRegistry;
import com.warsade.core.plugin.PluginRegistry;
import me.saiintbrisson.minecraft.AbstractView;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Optional;

public abstract class CorePlugin extends JavaPlugin {

    private static final PluginRegistry pluginRegistry = new PluginRegistry();
    private static final MenuRegistry menuRegistry = new MenuRegistry();

    @Override
    public void onEnable() {
        loadConfigs();
        super.onEnable();

        registerCommands();
        registerListeners();

        pluginRegistry.register(getDescription().getName(), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        pluginRegistry.unregister(getDescription().getName());
    }

    public void reload() {
    }

    public void loadConfigs() {
    }

    public void registerCommands() {
    }

    public void registerListeners() {
    }

    public void registerMenus(Menu<?>... menus) {
        for (Menu<?> menu : menus) {
            menuRegistry.register((Class<? extends Menu<?>>) menu.getClass(), ViewFrame.of(this, (AbstractView) menu));
        }
    }

    public static MenuRegistry getMenuRegistry() {
        return menuRegistry;
    }

    public static Optional<CorePlugin> findPluginByName(String name) {
        return pluginRegistry.getByName(name);
    }

    public static List<CorePlugin> getPlugins() {
        return pluginRegistry.getPlugins();
    }

}
