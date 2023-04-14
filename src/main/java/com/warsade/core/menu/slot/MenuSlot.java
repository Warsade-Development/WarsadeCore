package com.warsade.core.menu.slot;

import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface MenuSlot<T> {

    int getSlot();
    ItemStack getItem();

    T getData();

    Consumer<MenuSlotClick<T>> getClickAction();

}
