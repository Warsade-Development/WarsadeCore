package com.warsade.core.config.providers;

import com.warsade.core.utils.ItemBuilder;
import com.warsade.core.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemSchemaConfig {

    String key;
    String name;
    List<String> lore;
    String id;
    int data;
    boolean glow;
    int slot;

    public ItemSchemaConfig(String key, String name, List<String> lore, String id, int data, boolean glow) {
        this(key, name, lore, id, data, glow, 0);
    }

    public ItemSchemaConfig(String key, String name, List<String> lore, String id, int data, boolean glow, int slot) {
        this.key = key;
        this.name = name;
        this.lore = lore;
        this.id = id;
        this.data = data;
        this.glow = glow;
        this.slot = slot;
    }

    public ItemStack toItemStack() {
        ItemStack item = new ItemBuilder(Material.getMaterial(id)).setDurability(data)
                .setName(MessageUtils.replaceColor(name))
                .setLore(MessageUtils.replaceColor(lore))
                .build();

        if (glow) {
            item.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);

            ItemMeta meta = item.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public boolean isGlow() {
        return glow;
    }

    public void setGlow(boolean glow) {
        this.glow = glow;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public static ItemSchemaConfig buildItemSchemaByConfig(String path, String key, FileConfiguration config) {
        return new ItemSchemaConfig(
                key,
                config.getString(path + ".name"),
                config.getStringList(path + ".lore"),
                config.getString(path + ".id"),
                config.getInt(path + ".data"),
                config.getBoolean(path + ".glow", false),
                config.getInt(path + ".slot", 0)
        );
    }

}
