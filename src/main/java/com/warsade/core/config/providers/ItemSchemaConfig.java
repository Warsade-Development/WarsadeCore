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
    boolean useCustomNameAndLore;
    String name;
    List<String> lore;
    String id;
    int data;
    boolean glow;
    int amount;
    int slot;

    public ItemSchemaConfig(String key, boolean useCustomNameAndLore, String name, List<String> lore, String id, int data, boolean glow, int amount) {
        this(key, useCustomNameAndLore, name, lore, id, data, glow, 0, amount);
    }

    public ItemSchemaConfig(String key, boolean useCustomNameAndLore, String name, List<String> lore, String id, int data, boolean glow, int amount, int slot) {
        this.key = key;
        this.useCustomNameAndLore = useCustomNameAndLore;
        this.name = name;
        this.lore = lore;
        this.id = id;
        this.data = data;
        this.glow = glow;
        this.amount = amount;
        this.slot = slot;
    }

    public ItemStack toItemStack() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.getMaterial(id));
        itemBuilder.setDurability(data);

        if (isUseCustomNameAndLore()) {
            itemBuilder.setName(MessageUtils.replaceColor(name));
            itemBuilder.setLore(MessageUtils.replaceColor(lore));
        }

        itemBuilder.setAmount(amount);

        ItemStack item = itemBuilder.build();
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

    public boolean isUseCustomNameAndLore() {
        return useCustomNameAndLore;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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
                config.getBoolean(path + ".useCustomNameAndLore", true),
                config.getString(path + ".name", ""),
                config.getStringList(path + ".lore"),
                config.getString(path + ".id", ""),
                config.getInt(path + ".data", 0),
                config.getBoolean(path + ".glow", false),
                config.getInt(path + ".amount", 1),
                config.getInt(path + ".slot", 0)
        );
    }

    public static ItemSchemaConfig createCopyOf(ItemSchemaConfig other) {
        return new ItemSchemaConfig(
                other.getKey(),
                other.isUseCustomNameAndLore(),
                other.getName(),
                other.getLore(),
                other.getId(),
                other.getData(),
                other.isGlow(),
                other.getAmount(),
                other.getSlot()
        );
    }

}
