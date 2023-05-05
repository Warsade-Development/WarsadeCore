package com.warsade.core.config.providers;

import com.warsade.core.utils.ItemBuilder;
import com.warsade.core.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Item configuration example:
 *
 * itemKey:
 *    useCustomNameAndLore: true
 *    name: "&aItem name"
 *    lore:
 *      - "&fItem lore"
 *    id: "STONE"
 *    data: 0
 *    # enchantment:level
 *    enchantments:
 *        - "sharpness:1"
 *    glow: false
 *    amount: 1
 *    slot: 5
 *
 */
public class ItemSchemaConfig {

    String key;
    boolean useCustomNameAndLore;
    String name;
    List<String> lore;
    String id;
    int data;
    List<String> enchantments;
    boolean glow;
    int amount;
    int slot;

    public ItemSchemaConfig(String key, boolean useCustomNameAndLore, String name, List<String> lore, String id, int data, List<String> enchantments, boolean glow, int amount) {
        this(key, useCustomNameAndLore, name, lore, id, data, enchantments, glow, 0, amount);
    }

    public ItemSchemaConfig(String key, boolean useCustomNameAndLore, String name, List<String> lore, String id, int data, List<String> enchantments, boolean glow, int amount, int slot) {
        this.key = key;
        this.useCustomNameAndLore = useCustomNameAndLore;
        this.name = name;
        this.lore = lore;
        this.id = id;
        this.data = data;
        this.enchantments = enchantments;
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
        if (!enchantments.isEmpty()) {
            for (String selected : enchantments) {
                String[] input = selected.split(":");
                String enchantmentName = input[0];
                int level = Integer.parseInt(input[1]);

                Enchantment enchantment = EnchantmentWrapper.getByName(enchantmentName);
                if (enchantment != null) itemBuilder.addUnsafeEnchantment(enchantment, level);
            }
        }

        itemBuilder.setAmount(amount);

        ItemStack item = itemBuilder.build();
        if (glow && enchantments.isEmpty()) {
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

    public void setUseCustomNameAndLore(boolean useCustomNameAndLore) {
        this.useCustomNameAndLore = useCustomNameAndLore;
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

    public List<String> getEnchantments() {
        return enchantments;
    }

    public void setEnchantments(List<String> enchantments) {
        this.enchantments = enchantments;
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
                config.getStringList(path + ".enchantments"),
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
                other.getEnchantments(),
                other.isGlow(),
                other.getAmount(),
                other.getSlot()
        );
    }

}
