package com.warsade.core.bukkit.view;

import com.warsade.core.Core;
import com.warsade.core.config.providers.ItemSchemaConfig;
import com.warsade.core.config.providers.MenuConfig;
import com.warsade.core.menu.NormalMenu;
import com.warsade.core.menu.context.MenuContext;
import com.warsade.core.menu.context.PreOpenMenuContext;
import com.warsade.core.menu.slot.MenuSlot;
import com.warsade.core.menu.slot.impl.MenuSlotImpl;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class CustomMenu extends NormalMenu<MenuConfig> {

    public CustomMenu() {
    }

    Core plugin;

    public CustomMenu(MenuConfig menuConfig, Core plugin) {
        super(menuConfig);
        this.plugin = plugin;
    }

    @Override
    public Consumer<PreOpenMenuContext> onPreOpen(Player player, MenuConfig menuConfig) {
        return preOpenMenuContext -> {
            preOpenMenuContext.setInventoryTitle(menuConfig.getName());
            preOpenMenuContext.setInventoryRows(menuConfig.getRows());
        };
    }

    @Override
    public Consumer<MenuContext> onOpen(Player player, MenuConfig menuConfig) {
        return menuContext -> {
            for (ItemSchemaConfig item : menuConfig.getItems()) {
                MenuSlot<MenuConfig> itemSlot = new MenuSlotImpl<>(item.getSlot(), item.toItemStack(), onClick -> {
                    String itemPath = menuConfig.getPath() + ".items." + item.getKey();
                    FileConfiguration settings = plugin.getSettings().get();

                    if (settings.contains(itemPath + ".openAnotherMenu")) {
                        closeInventory(player);

                        String anotherMenu = settings.getString(itemPath + ".openAnotherMenu");
                        new CustomMenu().openInventory(player, new MenuConfig(settings, "CustomMenus." + anotherMenu));
                    }
                });

                menuContext.attachSlot(itemSlot);
            }
        };
    }

    @Override
    public void onClose(Player player) {
    }

}
