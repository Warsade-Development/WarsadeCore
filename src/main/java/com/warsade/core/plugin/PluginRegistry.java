package com.warsade.core.plugin;

import com.warsade.core.CorePlugin;
import com.warsade.core.plugin.exception.PluginAlreadyRegisteredException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PluginRegistry {

    HashMap<String, CorePlugin> plugins = new HashMap<>();

    public Optional<CorePlugin> getByName(String name) {
        return Optional.ofNullable(plugins.get(name));
    }

    public void register(String name, CorePlugin plugin) {
        if (getByName(name).isPresent()) {
            throw new PluginAlreadyRegisteredException();
        }

        plugins.put(name, plugin);
    }

    public void unregister(String name) {
        plugins.remove(name);
    }

    public List<CorePlugin> getPlugins() {
        return new ArrayList<>(plugins.values());
    }

}
