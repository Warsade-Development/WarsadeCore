package com.warsade.core.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Objects;

public class Config {

    private File file;
    private FileConfiguration fileConfiguration;

    private Plugin plugin;
    private String fileName;

    public Config(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName + ".yml";
    }

    public FileConfiguration get() {
        return fileConfiguration;
    }

    public void load() throws IOException, InvalidConfigurationException {
        File pluginFolder = new File(plugin.getDataFolder().getPath());
        if (!pluginFolder.exists()) pluginFolder.mkdirs();

        createNewFile();
        fileConfiguration = createFileConfigurationFromFile(file);

        if (!fileConfiguration.getString("version", "1.0.0").equals(plugin.getDescription().getVersion())) {
            migrateFileVersion();
        }
    }

    private void migrateFileVersion() {
        try {
            tryToMigrateToNewerFileVersion();
        } catch (InvalidConfigurationException | IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void tryToMigrateToNewerFileVersion() throws InvalidConfigurationException, IOException {
        HashMap<String, Object> existingValues = new HashMap<>();
        for (String key : fileConfiguration.getKeys(true)) {
            Object value = fileConfiguration.get(key);
            if (!(value instanceof ConfigurationSection)) existingValues.put(key, fileConfiguration.get(key));
        }

        if (file.delete()) {
            createNewFile();
            fileConfiguration = createFileConfigurationFromFile(file);

            setExistingValues(existingValues);

            fileConfiguration.set("version", plugin.getDescription().getVersion());
            fileConfiguration.save(file);
        } else plugin.getLogger().info("Cannot migrate file " + fileName);
    }
    private void setExistingValues(HashMap<String, Object> existingValues) {
        existingValues.forEach((path, object) -> fileConfiguration.set(path, object));
    }

    private void createNewFile() {
        file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            InputStream inputStream = plugin.getResource(fileName);
            try {
                Files.copy(Objects.requireNonNull(inputStream), file.toPath());
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    private FileConfiguration createFileConfigurationFromFile(File file) {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        try {
            yamlConfiguration.load(file);
            return yamlConfiguration;
        } catch (IOException | InvalidConfigurationException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void reload() {
        try {
            load();
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}