package com.warsade.core.menu.slot;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface MenuSlotClick {

    Player getPlayer();

    ItemStack getItem();

    int getSlot();

}
