package com.warsade.core.menu;

import com.warsade.core.menu.slot.MenuSlot;
import me.saiintbrisson.minecraft.ViewContext;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Menu <T> {

    String getInventoryTitle();

    void openInventory(Player player, T data);
    void closeInventory(Player player);

    MenuSlot getSlot(int slot);
    MenuSlot getSlotByItemStack(ItemStack itemStack);

    void attachSlot(MenuSlot menuSlot, ViewContext viewContext);

}
