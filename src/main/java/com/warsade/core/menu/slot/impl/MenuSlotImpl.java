package com.warsade.core.menu.slot.impl;

import com.warsade.core.menu.slot.MenuSlot;
import com.warsade.core.menu.slot.MenuSlotClick;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class MenuSlotImpl<T> implements MenuSlot<T> {

    int slot;
    ItemStack itemStack;

    T data;

    Consumer<MenuSlotClick<T>> clickAction;

    public MenuSlotImpl(int slot, ItemStack itemStack, Consumer<MenuSlotClick<T>> clickAction) {
        this(slot, itemStack, null, clickAction);
    }

    public MenuSlotImpl(int slot, ItemStack itemStack, T data, Consumer<MenuSlotClick<T>> clickAction) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.data = data;
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
    public T getData() {
        return data;
    }

    @Override
    public Consumer<MenuSlotClick<T>> getClickAction() {
        return clickAction;
    }

}
