package com.warsade.core.menu.slot.impl;

import com.warsade.core.menu.slot.MenuSlot;
import com.warsade.core.menu.slot.MenuSlotClick;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MenuSlotClickImpl<T> implements MenuSlotClick<T> {

    MenuSlot<T> menuSlot;

    Player player;
    ItemStack itemStack;
    int slot;

    public MenuSlotClickImpl(MenuSlot<T> menuSlot, Player player, ItemStack itemStack, int slot) {
        this.menuSlot = menuSlot;
        this.player = player;
        this.itemStack = itemStack;
        this.slot = slot;
    }

    @Override
    public MenuSlot<T> getMenuSlot() {
        return menuSlot;
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
