package com.warsade.core.menu.slot;

import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface MenuSlot {

    int getSlot();
    ItemStack getItem();

    Consumer<MenuSlotClick> getClickAction();

}
