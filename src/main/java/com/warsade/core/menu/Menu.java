package com.warsade.core.menu;

import com.warsade.core.menu.context.MenuContext;
import com.warsade.core.menu.slot.MenuSlot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public interface Menu <T> {

    String getInventoryTitle();

    void openInventory(Player player);
    void openInventory(Player player, T data);
    void openInventory(Player player, HashMap<String, Object> initialData, T object);
    void closeInventory(Player player);

    MenuSlot getSlot(int slot);
    MenuSlot getSlotByItemStack(ItemStack itemStack);

    void attachSlot(MenuSlot menuSlot, MenuContext menuContext);

}
