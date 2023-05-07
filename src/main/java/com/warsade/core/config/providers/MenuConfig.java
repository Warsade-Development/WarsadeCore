package com.warsade.core.config.providers;

import com.warsade.core.config.Config;
import com.warsade.core.utils.MessageUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuConfig {

    String path;

    String name;
    int rows;
    MenuLayout menuLayout = null;

    HashMap<String, String> itemsKeys = new HashMap<>();

    FileConfiguration configuration;

    /**
     * Will create a new menu config instance
     * with the path "Menus." + key
     */
    public MenuConfig(Config config, String key) {
        this(config.get(), "Menus." + key);
    }

    public MenuConfig(FileConfiguration configuration, String path) {
        this.path = path;
        this.name = MessageUtils.replaceColor(configuration.getString(path + ".name"));
        this.rows = configuration.getInt(path + ".rows");
        this.configuration = configuration;

        ConfigurationSection itemsSection = configuration.getConfigurationSection(path + ".items");
        if (itemsSection != null) for (String item : itemsSection.getKeys(false)) {
            itemsKeys.put(item, path + ".items." + item);
        }

        if (configuration.contains(path + ".layout")) menuLayout = new MenuLayout(path, this, configuration);
    }

    public MenuConfig(String path, String name, int rows) {
        this.path = path;
        this.name = name;
        this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public boolean hasLayout() {
        return menuLayout != null;
    }

    public MenuLayout getMenuLayout() {
        return menuLayout;
    }

    public List<ItemSchemaConfig> getItems() {
        List<ItemSchemaConfig> items = new ArrayList<>();
        itemsKeys.forEach((key, path) -> {
            items.add(ItemSchemaConfig.buildItemSchemaByConfig(path, key, configuration));
        });

        return items;
    }

    public static class MenuLayout {

        String[] value;
        HashMap<String, String> placeholders = new HashMap<>();

        final MenuConfig menuConfig;
        final FileConfiguration configuration;

        public MenuLayout(String menuPath, MenuConfig menuConfig, FileConfiguration configuration) {
            this.menuConfig = menuConfig;
            this.configuration = configuration;

            String path = menuPath + ".layout";
            this.value = configuration.getStringList(path + ".value").toArray(new String[menuConfig.getRows()]);

            ConfigurationSection itemsSection = configuration.getConfigurationSection(path + ".placeholders");
            if (itemsSection != null) for (String item : itemsSection.getKeys(false)) {
                placeholders.put(item, path + ".placeholders." + item);
            }
        }

        public String[] getValue() {
            return value;
        }

        public List<ItemSchemaConfig> getPlaceholdersItems() {
            List<ItemSchemaConfig> items = new ArrayList<>();
            placeholders.forEach((key, path) -> {
                items.add(ItemSchemaConfig.buildItemSchemaByConfig(path, key, configuration));
            });

            return items;
        }

    }

}