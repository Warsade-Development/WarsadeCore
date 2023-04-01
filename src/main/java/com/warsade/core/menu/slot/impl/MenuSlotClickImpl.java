package com.warsade.core.menu.slot.impl;

import com.warsade.core.menu.slot.MenuSlotClick;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MenuSlotClickImpl implements MenuSlotClick {

    Player player;
    ItemStack itemStack;
    int slot;

    public MenuSlotClickImpl(Player player, ItemStack itemStack, int slot) {
        this.player = player;
        this.itemStack = itemStack;
        this.slot = slot;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public ItemStack getItem() {
        return itemStack;
    }

    @Override
    public int getSlot() {
        return slot;
    }

}
