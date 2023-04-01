package com.warsade.core.menu.slot.impl;

import com.warsade.core.menu.slot.MenuSlot;
import com.warsade.core.menu.slot.MenuSlotClick;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class MenuSlotImpl implements MenuSlot {

    int slot;
    ItemStack itemStack;

    Consumer<MenuSlotClick> clickAction;

    public MenuSlotImpl(int slot, ItemStack itemStack, Consumer<MenuSlotClick> clickAction) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.clickAction = clickAction;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public ItemStack getItem() {
        return itemStack;
    }

    @Override
    public Consumer<MenuSlotClick> getClickAction() {
        return clickAction;
    }

}
