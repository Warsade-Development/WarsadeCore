package com.warsade.core.menu.slot;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public interface MenuSlotClick<T> {

    MenuSlot<T> getMenuSlot();
    ClickType getClickType();

    Player getPlayer();

    ItemStack getItem();

    int getSlot();

}
