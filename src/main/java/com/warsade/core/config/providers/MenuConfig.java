package com.warsade.core.config.providers;

import com.warsade.core.config.Config;
import com.warsade.core.utils.MessageUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuConfig {

    String name;
    int rows;

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
        this.name = MessageUtils.replaceColor(configuration.getString(path + ".name"));
        this.rows = configuration.getInt(path + ".rows");
        this.configuration = configuration;

        ConfigurationSection itemsSection = configuration.getConfigurationSection(path + ".items");
        if (itemsSection != null) for (String item : itemsSection.getKeys(false)) {
            itemsKeys.put(item, path + ".items." + item);
        }
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public List<ItemSchemaConfig> getItems() {
        List<ItemSchemaConfig> items = new ArrayList<>();
        itemsKeys.forEach((key, path) -> {
            items.add(ItemSchemaConfig.buildItemSchemaByConfig(path, key, configuration));
        });

        return items;
    }

}