package com.warsade.core.menu;

import com.warsade.core.config.providers.MenuConfig;
import com.warsade.core.menu.registry.MenuRegistry;
import com.warsade.core.menu.slot.MenuSlot;
import com.warsade.core.menu.slot.MenuSlotClick;
import com.warsade.core.menu.slot.impl.MenuSlotClickImpl;
import com.warsade.core.menu.slot.impl.MenuSlotImpl;
import me.saiintbrisson.minecraft.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public abstract class PaginatedMenu<T, K> extends PaginatedView<T> implements Menu<K> {

    MenuConfig menuConfig;
    Plugin plugin;
    MenuRegistry menuRegistry;

    public PaginatedMenu() {}

    public PaginatedMenu(MenuConfig menuConfig, Plugin plugin, MenuRegistry menuRegistry, String... layout) {
        super(6, menuConfig.getName());
        setCancelOnClick(true);

        this.menuConfig = menuConfig;
        this.plugin = plugin;
        this.menuRegistry = menuRegistry;

        setLayout(layout);
        setPreviousPageItem((context, viewItem) -> {
            if (!context.hasPreviousPage()) return;

            menuConfig.getItems().stream().filter(itemSchema -> itemSchema.getKey().equalsIgnoreCase("previous")).findFirst().ifPresent(itemSchema -> {
                viewItem.withSlot(1);
                viewItem.withItem(itemSchema.toItemStack());
            });
        });
        setNextPageItem((context, viewItem) -> {
            if (!context.hasNextPage()) return;

            menuConfig.getItems().stream().filter(itemSchema -> itemSchema.getKey().equalsIgnoreCase("next")).findFirst().ifPresent(itemSchema -> {
                viewItem.withSlot(2);
                viewItem.withItem(itemSchema.toItemStack());
            });
        });
    }

    public String getInventoryTitle() {
        return menuConfig.getName();
    }

    @Override
    public abstract void openInventory(Player player, K data);

    @Override
    public abstract void closeInventory(Player player);

    public abstract void onItemClick(MenuSlotClick onClick);

    public void openForPlayer(Player player, List<T> items, Consumer<ViewContext> setup) {
        menuRegistry.getViewFrameByMenuClass((Class<? extends Menu<?>>) this.getClass()).ifPresent(viewFrame -> {
            HashMap<String, Object> data = new HashMap<>();
            data.put("data", items);
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
            MenuSlotClick menuSlotClick = new MenuSlotClickImpl(viewSlotClickContext.getPlayer(), viewSlotClickContext.getItemWrapper().asBukkitItem(), viewSlotClickContext.getSlot());
            if (menuSlot.getClickAction() != null) menuSlot.getClickAction().accept(menuSlotClick);
        });
    }

    @Override
    protected void onRender(@NotNull ViewContext context) {
        super.onRender(context);
        context.paginated().setSource((List<?>) context.get("data"));

        ((Consumer<ViewContext>) context.get("setup")).accept(context);
    }

    @Override
    protected void onItemRender(@NotNull PaginatedViewSlotContext<T> paginatedViewSlotContext, @NotNull ViewItem viewItem, @NotNull T value) {
        viewItem.withItem(value)
                .onRender(render -> render.setItem(getViewItemStack(value)))
                .onClick(viewSlotClickContext -> {
                    MenuSlotClick menuSlotClick = new MenuSlotClickImpl(viewSlotClickContext.getPlayer(), viewSlotClickContext.getCurrentItem().asBukkitItem(), viewSlotClickContext.getSlot());
                    onItemClick(menuSlotClick);
                });
    }

    public abstract ItemStack getViewItemStack(T value);

    public MenuConfig getMenuConfig() {
        return menuConfig;
    }

    public Plugin getPlugin() {
        return plugin;
    }

}
