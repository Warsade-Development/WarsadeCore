package com.warsade.core.menu;

import com.warsade.core.config.providers.MenuConfig;
import com.warsade.core.menu.registry.MenuRegistry;
import com.warsade.core.menu.slot.MenuSlot;
import com.warsade.core.menu.slot.MenuSlotClick;
import com.warsade.core.menu.slot.impl.MenuSlotClickImpl;
import com.warsade.core.menu.slot.impl.MenuSlotImpl;
import me.saiintbrisson.minecraft.AbstractView;
import me.saiintbrisson.minecraft.View;
import me.saiintbrisson.minecraft.ViewContext;
import me.saiintbrisson.minecraft.ViewItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public abstract class NormalMenu <T> extends View implements Menu<T> {

    MenuConfig menuConfig;
    Plugin plugin;
    MenuRegistry menuRegistry;

    public NormalMenu() {}

    public NormalMenu(MenuConfig menuConfig, Plugin plugin, MenuRegistry menuRegistry) {
        super(menuConfig.getRows(), menuConfig.getName());
        setCancelOnClick(true);

        this.menuConfig = menuConfig;
        this.plugin = plugin;
        this.menuRegistry = menuRegistry;
    }

    @Override
    public String getInventoryTitle() {
        return menuConfig.getName();
    }

    @Override
    public abstract void openInventory(Player player, T data);

    @Override
    public abstract void closeInventory(Player player);

    public void openForPlayer(Player player, Consumer<ViewContext> setup) {
        openForPlayer(player, Collections.emptyMap(), setup);
    }

    public void openForPlayer(Player player, Map<String, Object> data, Consumer<ViewContext> setup) {
        menuRegistry.getViewFrameByMenuClass((Class<? extends Menu<?>>) this.getClass()).ifPresent(viewFrame -> {
            data.put("setup", setup);
            viewFrame.open(this.getClass(), player, data);
        });
    }

    public void closeForPlayer(Player player) {
        menuRegistry.getViewFrameByMenuClass((Class<? extends Menu<?>>) this.getClass()).ifPresent(viewFrame -> {
            AbstractView view = viewFrame.get(player);
            if (view != null) view.closeUninterruptedly();
        });
    }

    @Override
    protected void onRender(@NotNull ViewContext context) {
        super.onRender(context);
        setupInventory(context);
    }

    private void setupInventory(ViewContext context) {
        ((Consumer<ViewContext>) context.get("setup")).accept(context);
    }

    @Override
    public MenuSlot getSlot(int slot) {
        ViewItem viewItem = slot(slot);

        if (viewItem.getItem() == null) return null;
        else return new MenuSlotImpl(slot, (ItemStack) viewItem.getItem(), null);
    }

    @Override
    public MenuSlot getSlotByItemStack(ItemStack itemStack) {
        ViewItem viewItem = Arrays.stream(getItems()).filter(item -> item.getItem() == itemStack).findFirst().orElse(null);

        if (viewItem == null) return null;
        else return new MenuSlotImpl(viewItem.getSlot(), (ItemStack) viewItem.getItem(), null);
    }

    @Override
    public void attachSlot(MenuSlot menuSlot, ViewContext viewContext) {
        viewContext.slot(menuSlot.getSlot(), menuSlot.getItem()).onClick(viewSlotClickContext -> {
            MenuSlotClick menuSlotClick = new MenuSlotClickImpl(viewSlotClickContext.getPlayer(), viewSlotClickContext.getCurrentItem().asBukkitItem(), viewSlotClickContext.getSlot());
            if (menuSlot.getClickAction() != null) menuSlot.getClickAction().accept(menuSlotClick);
        });
    }

    public MenuConfig getMenuConfig() {
        return menuConfig;
    }

    public Plugin getPlugin() {
        return plugin;
    }

}