package com.warsade.core.config.providers;

import com.warsade.core.config.Config;
import com.warsade.core.utils.MessageUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MenuConfig {

    String name;
    int rows;

    List<ItemSchemaConfig> items = new ArrayList<>();

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

        for (String item : configuration.getConfigurationSection(path + ".items").getKeys(false)) {
            items.add(ItemSchemaConfig.buildItemSchemaByConfig(path + ".items." + item, item, configuration));
        }
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public List<ItemSchemaConfig> getItems() {
        return items;
    }

}