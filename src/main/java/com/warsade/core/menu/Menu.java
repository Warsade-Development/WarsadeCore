package com.warsade.core.menu;

import com.warsade.core.menu.context.MenuContext;
import com.warsade.core.menu.slot.MenuSlot;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public interface Menu<T> {

    String getInventoryTitle();

    void openInventory(Player player);
    void openInventory(Player player, T data);
    void openInventory(Player player, HashMap<String, Object> initialData, T object);
    void closeInventory(Player player);

    MenuContext getMenuContextByPlayer(Player player);

    /**
     * Add a new item to this menu
     * @deprecated
     * This method has been moved to {@link MenuContext#attachSlot(MenuSlot)}
    */
    @Deprecated
    void attachSlot(MenuSlot<T> menuSlot, MenuContext menuContext);

    HashMap<UUID, MenuContext> getMenuContexts();

}
